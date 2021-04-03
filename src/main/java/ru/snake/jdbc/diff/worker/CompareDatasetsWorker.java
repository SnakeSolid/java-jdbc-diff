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
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;

import ru.snake.jdbc.diff.Message;
import ru.snake.jdbc.diff.algorithm.DiffList;
import ru.snake.jdbc.diff.algorithm.DiffListClassic;
import ru.snake.jdbc.diff.algorithm.DiffListGreedy;
import ru.snake.jdbc.diff.algorithm.DiffListItem;
import ru.snake.jdbc.diff.algorithm.DiffListTrivial;
import ru.snake.jdbc.diff.blob.BlobParserFactory;
import ru.snake.jdbc.diff.blob.DefaultBlobParserFactory;
import ru.snake.jdbc.diff.config.Configuration;
import ru.snake.jdbc.diff.config.DiffAlgorithm;
import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.ComparedRow;
import ru.snake.jdbc.diff.model.ComparedTable;
import ru.snake.jdbc.diff.model.ConnectionSettings;
import ru.snake.jdbc.diff.model.DataCell;
import ru.snake.jdbc.diff.model.MainModel;
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

	private final MainModel model;

	private final Configuration config;

	private final String queryText;

	private final ConnectionSettings leftConnectionSettings;

	private final ConnectionSettings rightConnectionSettings;

	private final DatasetUpdateWrapper datasetWrapper;

	/**
	 * Create new worker to perform building data-set from given query list.
	 *
	 * @param model
	 *            model
	 * @param config
	 *            configuration settings
	 * @param queryText
	 *            string with queries
	 */
	public CompareDatasetsWorker(final MainModel model, final Configuration config, final String queryText) {
		this.model = model;
		this.config = config;
		this.queryText = queryText;

		this.leftConnectionSettings = model.getLeftConnection();
		this.rightConnectionSettings = model.getRightConnection();
		this.datasetWrapper = new DatasetUpdateWrapper(model);
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

	/**
	 * Creates array of {@link URL} using given library path. If path is null
	 * returns empty array.
	 *
	 * @param libraryPath
	 *            library path
	 * @return array of {@link URL}
	 * @throws MalformedURLException
	 *             if library path is incorrect
	 */
	private URL[] buildParserLibraryUrls(final String libraryPath) throws MalformedURLException {
		if (libraryPath == null) {
			return new URL[] {};
		} else {
			return new URL[] { new URL(libraryPath) };
		}
	}

	/**
	 * Loads BLOB factory class from given class loader. If class is null
	 * returns default BLOB factory as fallback value.
	 *
	 * @param factoryClassLoader
	 *            BLOB factory class loader
	 * @param factoryClassName
	 *            BLOB factory class name
	 * @param defaultFactory
	 *            default BLOB factory
	 * @return BLOB factory
	 * @throws ReflectiveOperationException
	 *             if reflection error occurred
	 */
	private BlobParserFactory loadParserFactory(
		final URLClassLoader factoryClassLoader,
		final String factoryClassName,
		final BlobParserFactory defaultFactory
	) throws ReflectiveOperationException {
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

	/**
	 * Build data set name using query name, table and data set index.
	 *
	 * @param index
	 *            data set index
	 * @param queryName
	 *            query name
	 * @param tableName
	 *            table name
	 * @return data set name
	 */
	private String buildDatasetName(final int index, final String queryName, final String tableName) {
		if (queryName == null) {
			return String.format("%d. %s", index, tableName);
		} else {
			return String.format("%d. %s", index, queryName);
		}
	}

	/**
	 * Loads driver using given class name from given class loader.
	 *
	 * @param classLoader
	 *            driver class loader
	 * @param driverClassName
	 *            driver class name
	 * @return driver instance
	 * @throws ReflectiveOperationException
	 *             if reflection error occurred
	 */
	private Driver loadDriver(final URLClassLoader classLoader, final String driverClassName)
			throws ClassNotFoundException, ReflectiveOperationException {
		Class<?> leftDriverClass = classLoader.loadClass(driverClassName);

		if (Driver.class.isAssignableFrom(leftDriverClass)) {
			Driver driver = (Driver) leftDriverClass.getConstructor().newInstance();

			return driver;
		}

		throw new RuntimeException("Driver class " + driverClassName + " does not implement java.sql.Driver.");
	}

	/**
	 * Read query result from database and build corresponding compared data
	 * set.
	 *
	 * @param datasetName
	 *            data set name
	 * @param tableName
	 *            table name
	 * @param leftStatement
	 *            left prepared statement
	 * @param rightStatement
	 *            right prepared statement
	 * @param leftFactory
	 *            left BLOB factory
	 * @param rightFactory
	 *            right BLOB factory
	 * @param aQueryText
	 *            single query text
	 * @return compared data set
	 * @throws SQLException
	 *             if SQL error occurred
	 */
	private ComparedDataset buildDataset(
		final String datasetName,
		final String tableName,
		final Statement leftStatement,
		final Statement rightStatement,
		final BlobParserFactory leftFactory,
		final BlobParserFactory rightFactory,
		final String aQueryText
	) throws SQLException {
		List<String> columnNames;
		List<List<TableCell>> leftRows;
		List<List<TableCell>> rightRows;

		try (ResultSet leftResultSet = leftStatement.executeQuery(aQueryText);
				ResultSet rightResultSet = rightStatement.executeQuery(aQueryText)) {
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

		int nCellsEquals = config.getRowSimilarity() * columnNames.size() / 100;
		DiffList<List<TableCell>> diffList = buildDiffAlgorithm(leftRows, rightRows, nCellsEquals);
		List<DiffListItem<List<TableCell>>> diff = diffList.diff();
		ComparedDataset dataset = createComparedDataset(datasetName, diff, columnNames);

		return dataset;
	}

	/**
	 * Creates diff algorithm according configuration settings.
	 *
	 * @param leftRows
	 *            left rows
	 * @param rightRows
	 *            right rows
	 * @param nCellsEquals
	 *            number of equal cell for row similarity
	 * @return diff algorithm
	 */
	private DiffList<List<TableCell>> buildDiffAlgorithm(
		final List<List<TableCell>> leftRows,
		final List<List<TableCell>> rightRows,
		final int nCellsEquals
	) {
		DiffAlgorithm diffAlgorithm = Optional.ofNullable(model.getDiffAlgorithm()).orElseGet(config::getDiffAlgorithm);
		RowsEqualsPredicate predicate;

		switch (diffAlgorithm) {
		case CLASSIC:
			predicate = new RowsEqualsPredicate(nCellsEquals);

			return new DiffListClassic<>(leftRows, rightRows, predicate);

		case GREEDY:
			predicate = new RowsEqualsPredicate(nCellsEquals);

			return new DiffListGreedy<>(leftRows, rightRows, predicate);

		case TRIVIAL:
			return new DiffListTrivial<>(leftRows, rightRows);

		default:
			throw new RuntimeException("Corresponding diff algorithm was not found for " + diffAlgorithm);
		}
	}

	/**
	 * Creates compared data set from difference between two tables.
	 *
	 * @param name
	 *            data set name
	 * @param diff
	 *            table difference
	 * @param columnNames
	 *            column names
	 * @return compared data set
	 */
	private ComparedDataset createComparedDataset(
		final String name,
		final List<DiffListItem<List<TableCell>>> diff,
		final List<String> columnNames
	) {
		List<ComparedRow> leftRows = new ArrayList<>();
		List<ComparedRow> rightRows = new ArrayList<>();

		for (DiffListItem<List<TableCell>> diffItem : diff) {
			leftRows.add(createDataRow(diffItem.getLeft(), diffItem.getRight()));
			rightRows.add(createDataRow(diffItem.getRight(), diffItem.getLeft()));
		}

		ComparedTable leftTable = ComparedTable.create(leftRows);
		ComparedTable rightTable = ComparedTable.create(rightRows);

		return ComparedDataset.create(name, columnNames, leftTable, rightTable);
	}

	/**
	 * Creates single data row from single road of table difference.
	 *
	 * @param thisRow
	 *            this difference row
	 * @param otherRow
	 *            other difference row
	 * @return data table row
	 */
	private ComparedRow createDataRow(final List<TableCell> thisRow, final List<TableCell> otherRow) {
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

		return ComparedRow.create(dataRow);
	}

	/**
	 * Read data from given {@link ResultSet}.
	 *
	 * @param resultSet
	 *            result set
	 * @param mappers
	 *            column mappers
	 * @return parsed table data
	 * @throws SQLException
	 *             if SQL error occurred
	 */
	private List<List<TableCell>> readRows(final ResultSet resultSet, final List<ColumnMapper> mappers)
			throws SQLException {
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

		model.setExecuting(false);
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
