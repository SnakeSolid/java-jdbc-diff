package ru.snake.jdbc.diff.worker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import ru.snake.jdbc.diff.blob.BlobParser;
import ru.snake.jdbc.diff.worker.TableCell;

public class BinaryMapper implements ColumnMapper {

	private final int index;

	private final BlobParser parser;

	public BinaryMapper(final int index, final BlobParser parser) {
		this.index = index;
		this.parser = parser;
	}

	@Override
	public TableCell map(ResultSet resultSet) throws SQLException {
		byte[] value = resultSet.getBytes(index);

		if (resultSet.wasNull()) {
			return TableCell.empty();
		} else if (parser != null) {
			return TableCell.binary(value, parser.parse(value));
		} else {
			return TableCell.binary(value, null);
		}
	}

	@Override
	public String toString() {
		return "BinaryMapper [index=" + index + ", parser=" + parser + "]";
	}

}
