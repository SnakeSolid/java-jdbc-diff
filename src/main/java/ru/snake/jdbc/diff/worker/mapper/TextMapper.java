package ru.snake.jdbc.diff.worker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import ru.snake.jdbc.diff.worker.TableCell;

public class TextMapper implements ColumnMapper {

	private final String fieldName;

	public TextMapper(final String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public TableCell map(ResultSet resultSet) throws SQLException {
		Object value = resultSet.getObject(fieldName);

		if (resultSet.wasNull()) {
			return TableCell.empty();
		} else {
			String text = String.valueOf(value);

			return TableCell.text(text);
		}
	}

	@Override
	public String toString() {
		return "TextMapper [fieldName=" + fieldName + "]";
	}

}
