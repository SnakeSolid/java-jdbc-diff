package ru.snake.jdbc.diff.worker.mapper;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ru.snake.jdbc.diff.algorithm.DiffListClassic;
import ru.snake.jdbc.diff.algorithm.DiffListItem;
import ru.snake.jdbc.diff.algorithm.DiffType;
import ru.snake.jdbc.diff.blob.BlobParser;
import ru.snake.jdbc.diff.blob.BlobParserFactory;

public final class MapperBuilder {

	private final ResultSetMetaData leftMetaData;

	private final ResultSetMetaData rightMetaData;

	private final Set<String> leftBinaryTypes;

	private final Set<String> rightBinaryTypes;

	private final BlobParserFactory leftFactory;

	private final BlobParserFactory rightFactory;

	public MapperBuilder(
		final ResultSetMetaData leftMetaData,
		final ResultSetMetaData rightMetaData,
		final Set<String> leftBinaryTypes,
		final Set<String> rightBinaryTypes,
		final BlobParserFactory leftFactory,
		final BlobParserFactory rightFactory
	) {
		this.leftMetaData = leftMetaData;
		this.rightMetaData = rightMetaData;
		this.leftBinaryTypes = leftBinaryTypes;
		this.rightBinaryTypes = rightBinaryTypes;
		this.leftFactory = leftFactory;
		this.rightFactory = rightFactory;
	}

	/**
	 * Create column mappers for both result sets.
	 *
	 * @param tableName
	 *            table name
	 * @return column mappers for both tables
	 * @throws SQLException
	 *             if error occurred
	 */
	public Mappers build(final String tableName) throws SQLException {
		List<ColumnMapper> leftMappers = new ArrayList<>();
		List<ColumnMapper> rightMappers = new ArrayList<>();
		List<String> leftColumns = new ArrayList<>();
		List<String> rightColumns = new ArrayList<>();

		for (int index = 1; index <= leftMetaData.getColumnCount(); index += 1) {
			String fieldName = leftMetaData.getColumnName(index);
			String fieldType = leftMetaData.getColumnTypeName(index);

			if (leftBinaryTypes.contains(fieldType)) {
				BlobParser parser = leftFactory.getParser(tableName, fieldName, fieldType);

				leftMappers.add(new BinaryMapper(index, parser));
			} else {
				leftMappers.add(new TextMapper(index));
			}

			leftColumns.add(fieldName);
		}

		for (int index = 1; index <= rightMetaData.getColumnCount(); index += 1) {
			String fieldName = rightMetaData.getColumnName(index);
			String fieldType = rightMetaData.getColumnTypeName(index);

			if (rightBinaryTypes.contains(fieldType)) {
				BlobParser parser = rightFactory.getParser(tableName, fieldName, fieldType);

				rightMappers.add(new BinaryMapper(index, parser));
			} else {
				rightMappers.add(new TextMapper(index));
			}

			rightColumns.add(fieldName);
		}

		List<DiffListItem<String>> columnDiff = new DiffListClassic<String>(leftColumns, rightColumns, Objects::equals)
			.diff();
		Iterator<ColumnMapper> leftIterator = leftMappers.iterator();
		Iterator<ColumnMapper> rightIterator = rightMappers.iterator();
		List<ColumnMapper> allLeftMappers = new ArrayList<>();
		List<ColumnMapper> allRightMappers = new ArrayList<>();
		List<String> allColumns = new ArrayList<>();

		for (DiffListItem<String> diff : columnDiff) {
			DiffType type = diff.getType();

			switch (type) {
			case BOTH:
				allLeftMappers.add(leftIterator.next());
				allRightMappers.add(rightIterator.next());
				allColumns.add(diff.getLeft());
				break;

			case LEFT:
				allLeftMappers.add(leftIterator.next());
				allRightMappers.add(new EmptyMapper());
				allColumns.add(diff.getLeft());
				break;

			case RIGHT:
				allLeftMappers.add(new EmptyMapper());
				allRightMappers.add(rightIterator.next());
				allColumns.add(diff.getRight());
				break;

			default:
				throw new RuntimeException("Unexpected diff type - " + type);
			}
		}

		return new Mappers(allLeftMappers, allRightMappers, allColumns);
	}

	@Override
	public String toString() {
		return "MapperBuilder [leftMetaData=" + leftMetaData + ", rightMetaData=" + rightMetaData + ", leftBinaryTypes="
				+ leftBinaryTypes + ", rightBinaryTypes=" + rightBinaryTypes + ", leftFactory=" + leftFactory
				+ ", rightFactory=" + rightFactory + "]";
	}

}
