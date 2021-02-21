package ru.snake.jdbc.diff.worker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import ru.snake.jdbc.diff.worker.TableCell;

public class BinaryMapper implements ColumnMapper {

	private final String fieldName;

	public BinaryMapper(final String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public TableCell map(ResultSet resultSet) throws SQLException {
		byte[] value = resultSet.getBytes(fieldName);

		if (resultSet.wasNull()) {
			return TableCell.empty();
		} else {
			return TableCell.binary(value);
		}
	}

	@Override
	public String toString() {
		return "BinaryMapper [fieldName=" + fieldName + "]";
	}
}
