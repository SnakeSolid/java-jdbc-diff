package ru.snake.jdbc.diff.worker;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
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
import ru.snake.jdbc.diff.worker.mapper.ColumnMapper;
import ru.snake.jdbc.diff.worker.mapper.MapperBuilder;
import ru.snake.jdbc.diff.worker.mapper.Mappers;
import ru.snake.jdbc.diff.worker.parse.QueryParser;
import ru.snake.jdbc.diff.worker.query.Query;
import ru.snake.jdbc.diff.worker.query.QueryTable;
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
		String rightDriverClassName = rightConnectionSettings.getDriverClass();
		String leftConnectionUrl = leftConnectionSettings.getUrl();
		String rightConnectionUrl = rightConnectionSettings.getUrl();
		String leftFactoryClass = leftConnectionSettings.getParserClass();
		String rightFactoryClass = rightConnectionSettings.getParserClass();
		URL[] leftUrls = new URL[] { new URL(leftConnectionSettings.getDriverPath()) };
		URL[] rightUrls = new URL[] { new URL(rightConnectionSettings.getDriverPath()) };
		URL[] leftFactoryUrls = buildParserLibraryUrls(leftConnectionSettings.getParserLibrary());
		URL[] rightFactoryUrls = buildParserLibraryUrls(rightConnectionSettings.getParserLibrary());
		BlobParserFactory parserFactory = new DefaultBlobParserFactory();

		try (URLClassLoader leftClassLoader = new URLClassLoader(leftUrls, ClassLoader.getSystemClassLoader());
				URLClassLoader rightClassLoader = new URLClassLoader(rightUrls, ClassLoader.getSystemClassLoader());
				URLClassLoader leftFactoryClassLoader = new URLClassLoader(
					leftFactoryUrls,
					ClassLoader.getSystemClassLoader()
				);
				URLClassLoader rightFactoryClassLoader = new URLClassLoader(
					rightFactoryUrls,
					ClassLoader.getSystemClassLoader()
				);) {
			Driver leftDriver = loadDriver(leftClassLoader, leftDriverClassName);
			Driver rightDriver = loadDriver(rightClassLoader, rightDriverClassName);
			BlobParserFactory leftFactory = loadParserFactory(leftFactoryClassLoader, leftFactoryClass, parserFactory);
			BlobParserFactory rightFactory = loadParserFactory(
				rightFactoryClassLoader,
				rightFactoryClass,
				parserFactory
			);

			try (Connection leftConnection = leftDriver.connect(leftConnectionUrl, info);
					Connection rightConnection = rightDriver.connect(rightConnectionUrl, info);
					Statement leftStatement = leftConnection.createStatement();
					Statement rightStatement = rightConnection.createStatement()) {
				int index = 1;

				for (Query query : queries) {
					String tableName = QueryTable.tableName(query).orElse("-");
					String datasetName = buildDatasetName(index, query.getQueryName(), tableName);
					ComparedDataset dataset = buildDataset(
						datasetName,
						tableName,
						leftStatement,
						rightStatement,
						leftFactory,
						rightFactory,
						query.getQueryText()
					);

					datasetWrapper.pushComparedDataset(dataset);

					index += 1;
				}
			}
		} finally {
			DriverDeregistrator.deregisterAll();
		}

		return errorMessages;
	}

	private URL[] buildParserLibraryUrls(String libraryPath) throws MalformedURLException {
		if (libraryPath == null) {
			return new URL[] {};
		} else {
			return new URL[] { new URL(libraryPath) };
		}
	}

	private BlobParserFactory loadParserFactory(
		final URLClassLoader factoryClassLoader,
		final String factoryClassName,
		final BlobParserFactory defaultFactory
	) throws ClassNotFoundException, ReflectiveOperationException {
		if (factoryClassName == null) {
			return defaultFactory;
		} else {
			Class<?> factoryClass = factoryClassLoader.loadClass(factoryClassName);

			if (BlobParserFactory.class.isAssignableFrom(factoryClass)) {
				BlobParserFactory factory = (BlobParserFactory) factoryClass.getConstructor().newInstance();

				return factory;
			}

			throw new RuntimeException(
				"BLOB parser factory class " + factoryClassName
						+ " does not implement ru.snake.jdbc.diff.blob.BlobParserFactory."
			);
		}
	}

	private String buildDatasetName(int index, String queryName, String tableName) {
		if (queryName == null) {
			return String.format("%d. %s", index, tableName);
		} else {
			return String.format("%d. %s", index, queryName);
		}
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
		final String datasetName,
		final String tableName,
		final Statement leftStatement,
		final Statement rightStatement,
		final BlobParserFactory leftFactory,
		final BlobParserFactory rightFactory,
		final String queryText
	) throws SQLException {
		List<String> columnNames;
		List<List<TableCell>> leftRows;
		List<List<TableCell>> rightRows;

		try (ResultSet leftResultSet = leftStatement.executeQuery(queryText);
				ResultSet rightResultSet = rightStatement.executeQuery(queryText)) {
			ResultSetMetaData leftMetaData = leftResultSet.getMetaData();
			ResultSetMetaData rightMetaData = rightResultSet.getMetaData();
			Mappers mappers = new MapperBuilder(
				leftMetaData,
				rightMetaData,
				leftConnectionSettings.getBinaryTypes(),
				rightConnectionSettings.getBinaryTypes(),
				leftFactory,
				rightFactory
			).build(tableName);

			leftRows = readRows(leftResultSet, mappers.getLeftMappers());
			rightRows = readRows(rightResultSet, mappers.getRightMappers());
			columnNames = mappers.getAllColumns();
		}

		int nRowsEquals = config.getRowSimilarity() * columnNames.size() / 100;
		RowsEqualsPredicate predicate = new RowsEqualsPredicate(nRowsEquals);
		List<DiffListItem<List<TableCell>>> diff = new DiffList<>(leftRows, rightRows, predicate).diff();
		ComparedDataset dataset = createComparedDataset(datasetName, diff, columnNames);

		return dataset;
	}

	private ComparedDataset
			createComparedDataset(String name, List<DiffListItem<List<TableCell>>> diff, List<String> columnNames) {
		List<List<DataCell>> left = new ArrayList<>();
		List<List<DataCell>> right = new ArrayList<>();

		for (DiffListItem<List<TableCell>> diffItem : diff) {
			left.add(createDataRow(diffItem.getLeft(), diffItem.getRight()));
			right.add(createDataRow(diffItem.getRight(), diffItem.getLeft()));
		}

		return new ComparedDataset(name, columnNames, left, right);
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

				dataRow.add(DataCell.valid(cell.getText(), cell.getObject()));
			}
		} else {
			for (int index = 0; index < thisRow.size(); index += 1) {
				TableCell thisCell = thisRow.get(index);
				TableCell otherCell = otherRow.get(index);
				String thisText = thisCell.getText();
				String otherText = otherCell.getText();

				if (Objects.equals(thisText, otherText)) {
					dataRow.add(DataCell.valid(thisText, thisCell.getObject()));
				} else {
					dataRow.add(DataCell.changed(thisText, thisCell.getObject()));
				}
			}
		}

		return dataRow;
	}

	private List<List<TableCell>> readRows(ResultSet resultSet, List<ColumnMapper> mappers) throws SQLException {
		List<List<TableCell>> rows = new ArrayList<>();

		while (resultSet.next()) {
			List<TableCell> row = new ArrayList<>(mappers.size());

			for (ColumnMapper mapper : mappers) {
				row.add(mapper.map(resultSet));
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
