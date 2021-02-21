package ru.snake.jdbc.diff.worker;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;

import ru.snake.jdbc.diff.Message;
import ru.snake.jdbc.diff.algorithm.DiffList;
import ru.snake.jdbc.diff.algorithm.DiffListItem;
import ru.snake.jdbc.diff.blob.BlobParserFactory;
import ru.snake.jdbc.diff.blob.DefaultBlobParserFactory;
import ru.snake.jdbc.diff.config.Configuration;
import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.ConnectionSettings;
import ru.snake.jdbc.diff.model.DataCell;
import ru.snake.jdbc.diff.worker.driver.DriverDeregistrator;
import ru.snake.jdbc.diff.worker.mapper.BinaryMapper;
import ru.snake.jdbc.diff.worker.mapper.ColumnMapper;
import ru.snake.jdbc.diff.worker.mapper.TextMapper;
import ru.snake.jdbc.diff.worker.parse.QueryParser;
import ru.snake.jdbc.diff.worker.query.Query;
import ru.snake.jdbc.diff.worker.wrapper.DatasetUpdateWrapper;

/**
 * Background worker. Worker read queries from text, executes every query using
 * given connection setting. All retrieved data-sets will be converted to DBUnit
 * XML representation.
 *
 * @author snake
 *
 */
public final class CompareDatasetsWorker extends SwingWorker<List<String>, Void> {

	private final Configuration config;

	private final String queryText;

	private final ConnectionSettings leftConnectionSettings;

	private final ConnectionSettings rightConnectionSettings;

	private final DatasetUpdateWrapper datasetWrapper;

	/**
	 * Create new worker to perform building data-set from given query list.
	 *
	 * @param config
	 *            configuration settings
	 * @param queryText
	 *            string with queries
	 * @param connectionSettings
	 *            connection settings
	 * @param outputDocument
	 *            output document
	 */
	public CompareDatasetsWorker(
		final Configuration config,
		final String queryText,
		final ConnectionSettings leftConnection,
		final ConnectionSettings rightConnection,
		final DatasetUpdateWrapper datasetWrapper
	) {
		this.config = config;
		this.queryText = queryText;
		this.leftConnectionSettings = leftConnection;
		this.rightConnectionSettings = rightConnection;
		this.datasetWrapper = datasetWrapper;
	}

	@Override
	protected List<String> doInBackground() throws Exception {
		List<Query> queries = QueryParser.parse(this.queryText);

		if (queries.isEmpty()) {
			return Collections.emptyList();
		}

		List<String> errorMessages = new ArrayList<>();
		Properties info = new Properties();

		String leftDriverClassName = leftConnectionSettings.getDriverClass();
		String leftConnectionUrl = leftConnectionSettings.getUrl();
		URL leftUrl = new URL(leftConnectionSettings.getDriverPath());
		URL[] leftUrls = new URL[] { leftUrl };

		String rightDriverClassName = rightConnectionSettings.getDriverClass();
		String rightConnectionUrl = rightConnectionSettings.getUrl();
		URL rightUrl = new URL(rightConnectionSettings.getDriverPath());
		URL[] rightUrls = new URL[] { rightUrl };

		BlobParserFactory parserFactory = new DefaultBlobParserFactory();

		try (URLClassLoader leftClassLoader = new URLClassLoader(leftUrls, ClassLoader.getSystemClassLoader());
				URLClassLoader rightClassLoader = new URLClassLoader(rightUrls, ClassLoader.getSystemClassLoader())) {
			Driver leftDriver = loadDriver(leftClassLoader, leftDriverClassName);
			Driver rightDriver = loadDriver(rightClassLoader, rightDriverClassName);

			try (Connection leftConnection = leftDriver.connect(leftConnectionUrl, info);
					Connection rightConnection = rightDriver.connect(rightConnectionUrl, info);
					Statement leftStatement = leftConnection.createStatement();
					Statement rightStatement = rightConnection.createStatement()) {
				for (Query query : queries) {
					ComparedDataset dataset = buildDataset(
						leftStatement,
						rightStatement,
						parserFactory,
						parserFactory,
						query.getQueryText()
					);

					datasetWrapper.pushComparedDataset(dataset);
				}
			}
		} finally {
			DriverDeregistrator.deregisterAll();
		}

		return errorMessages;
	}

	private Driver loadDriver(URLClassLoader classLoader, String driverClassName)
			throws ClassNotFoundException, ReflectiveOperationException {
		Class<?> leftDriverClass = classLoader.loadClass(driverClassName);

		if (Driver.class.isAssignableFrom(leftDriverClass)) {
			Driver driver = (Driver) leftDriverClass.getConstructor().newInstance();

			return driver;
		}

		throw new RuntimeException("Driver class " + driverClassName + " does not implement java.sql.Driver.");
	}

	private ComparedDataset buildDataset(
		final Statement leftStatement,
		final Statement rightStatement,
		final BlobParserFactory leftFactory,
		final BlobParserFactory rightFactory,
		final String queryText
	) throws SQLException {
		Set<String> columnNames;
		List<List<TableCell>> leftRows;
		List<List<TableCell>> rightRows;
		int nRowsEquals;

		try (ResultSet leftResultSet = leftStatement.executeQuery(queryText);
				ResultSet rightResultSet = rightStatement.executeQuery(queryText)) {
			Set<String> leftBinaryTypes = leftConnectionSettings.getBinaryTypes();
			Map<String, ColumnMapper> leftMappers = createColumnMappers(leftResultSet, leftBinaryTypes);
			Set<String> rightBinaryTypes = rightConnectionSettings.getBinaryTypes();
			Map<String, ColumnMapper> rightMappers = createColumnMappers(rightResultSet, rightBinaryTypes);

			columnNames = mergeColumnNames(leftMappers.keySet(), rightMappers.keySet());
			leftRows = readRows(leftResultSet, leftMappers, columnNames);
			rightRows = readRows(rightResultSet, rightMappers, columnNames);
			nRowsEquals = config.getRowSimilarity() * columnNames.size() / 100;
		}

		RowsEqualsPredicate predicate = new RowsEqualsPredicate(nRowsEquals);
		List<DiffListItem<List<TableCell>>> diff = new DiffList<>(leftRows, rightRows, predicate).diff();
		ComparedDataset dataset = createComparedDataset(diff, columnNames);

		return dataset;
	}

	private ComparedDataset createComparedDataset(List<DiffListItem<List<TableCell>>> diff, Set<String> columnNames) {
		String name = "Table Name";
		ArrayList<String> columnList = new ArrayList<>(columnNames);
		List<List<DataCell>> left = new ArrayList<>();
		List<List<DataCell>> right = new ArrayList<>();

		for (DiffListItem<List<TableCell>> diffItem : diff) {
			left.add(createDataRow(diffItem.getLeft(), diffItem.getRight()));
			right.add(createDataRow(diffItem.getRight(), diffItem.getLeft()));
		}

		return new ComparedDataset(name, columnList, left, right);
	}

	private List<DataCell> createDataRow(List<TableCell> thisRow, List<TableCell> otherRow) {
		List<DataCell> dataRow = new ArrayList<>();

		if (thisRow == null) {
			for (int index = 0; index < otherRow.size(); index += 1) {
				dataRow.add(DataCell.missing());
			}
		} else if (otherRow == null) {
			for (int index = 0; index < thisRow.size(); index += 1) {
				TableCell cell = thisRow.get(index);

				dataRow.add(DataCell.valid(cell.getText(), cell.getBinary()));
			}
		} else {
			for (int index = 0; index < thisRow.size(); index += 1) {
				TableCell thisCell = thisRow.get(index);
				TableCell otherCell = otherRow.get(index);
				String thisText = thisCell.getText();
				String otherText = otherCell.getText();

				if (Objects.equals(thisText, otherText)) {
					dataRow.add(DataCell.valid(thisText, thisCell.getBinary()));
				} else {
					dataRow.add(DataCell.changed(thisText, thisCell.getBinary()));
				}
			}
		}

		return dataRow;
	}

	private Set<String> mergeColumnNames(Collection<String> leftMappers, Collection<String> rightMappers) {
		Set<String> columnNames = new LinkedHashSet<>();
		columnNames.addAll(leftMappers);
		columnNames.addAll(rightMappers);

		return columnNames;
	}

	private Map<String, ColumnMapper> createColumnMappers(ResultSet resultSet, Set<String> binaryTypes)
			throws SQLException {
		ResultSetMetaData leftMetadata = resultSet.getMetaData();
		Map<String, ColumnMapper> mappers = new LinkedHashMap<>();

		for (int index = 1; index <= leftMetadata.getColumnCount(); index += 1) {
			String fieldName = leftMetadata.getColumnName(index);
			String fieldType = leftMetadata.getColumnTypeName(index);

			if (binaryTypes.contains(fieldType)) {
				mappers.put(fieldName, new BinaryMapper(fieldName));
			} else {
				mappers.put(fieldName, new TextMapper(fieldName));
			}
		}

		return mappers;
	}

	private List<List<TableCell>>
			readRows(ResultSet resultSet, Map<String, ColumnMapper> mappers, Set<String> columnNames)
					throws SQLException {
		List<List<TableCell>> rows = new ArrayList<>();

		while (resultSet.next()) {
			List<TableCell> row = new ArrayList<>(columnNames.size());

			for (String columnName : columnNames) {
				ColumnMapper mapper = mappers.get(columnName);

				if (mapper == null) {
					row.add(TableCell.empty());
				} else {
					row.add(mapper.map(resultSet));
				}
			}

			rows.add(row);
		}

		return rows;
	}

	@Override
	protected void done() {
		List<String> errorMessages;

		try {
			errorMessages = get();
		} catch (InterruptedException | ExecutionException e) {
			errorMessages = Collections.singletonList(unrollMessages(e));
		}

		if (!errorMessages.isEmpty()) {
			String errorMassage = errorMessages.stream().collect(Collectors.joining("\n"));

			Message.showError(errorMassage);
		}
	}

	/**
	 * Unroll exception messages to multi-line string.
	 *
	 * @param exception
	 *            exception
	 * @return formatted message
	 */
	private String unrollMessages(final Exception exception) {
		StringBuilder builder = new StringBuilder();
		Throwable cause = exception.getCause();

		builder.append("Error > ");
		builder.append(exception.getLocalizedMessage());

		if (cause != null) {
			builder.append("\n  Caused by > ");
			builder.append(cause.getLocalizedMessage());
		}

		for (Throwable suppressed : exception.getSuppressed()) {
			builder.append("\n  Suppressed > ");
			builder.append(suppressed.getLocalizedMessage());
		}

		return builder.toString();
	}

}
