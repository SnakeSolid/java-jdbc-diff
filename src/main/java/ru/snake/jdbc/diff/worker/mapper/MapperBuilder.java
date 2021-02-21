package ru.snake.jdbc.diff.worker.mapper;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.snake.jdbc.diff.blob.BlobParser;
import ru.snake.jdbc.diff.blob.BlobParserFactory;

public class MapperBuilder {

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

	public Mappers build(String tableName) throws SQLException {
		List<ColumnMapper> leftMappers = new ArrayList<>();
		List<ColumnMapper> rightMappers = new ArrayList<>();
		List<String> allColumns = new ArrayList<>();

		for (int index = 1; index <= leftMetaData.getColumnCount(); index += 1) {
			String fieldName = leftMetaData.getColumnName(index);
			String fieldType = leftMetaData.getColumnTypeName(index);

			if (leftBinaryTypes.contains(fieldType)) {
				BlobParser parser = leftFactory.getParser(tableName, fieldName, fieldType);

				leftMappers.add(new BinaryMapper(index, parser));
			} else {
				leftMappers.add(new TextMapper(index));
			}

			allColumns.add(fieldName);
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
		}

		// Hack to keep number of value the same for both tables.

		while (leftMappers.size() < rightMappers.size()) {
			leftMappers.add(new EmptyMapper());
		}

		while (rightMappers.size() < leftMappers.size()) {
			rightMappers.add(new EmptyMapper());
		}

		return new Mappers(leftMappers, rightMappers, allColumns);
	}

	@Override
	public String toString() {
		return "MapperBuilder [leftMetaData=" + leftMetaData + ", rightMetaData=" + rightMetaData + ", leftBinaryTypes="
				+ leftBinaryTypes + ", rightBinaryTypes=" + rightBinaryTypes + ", leftFactory=" + leftFactory
				+ ", rightFactory=" + rightFactory + "]";
	}

}
