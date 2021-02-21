package ru.snake.jdbc.diff.worker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import ru.snake.jdbc.diff.worker.TableCell;

public class TextMapper implements ColumnMapper {

	private final int index;

	public TextMapper(final int index) {
		this.index = index;
	}

	@Override
	public TableCell map(ResultSet resultSet) throws SQLException {
		Object value = resultSet.getObject(index);

		if (resultSet.wasNull()) {
			return TableCell.empty();
		} else {
			String text = String.valueOf(value);

			return TableCell.text(text);
		}
	}

	@Override
	public String toString() {
		return "TextMapper [index=" + index + "]";
	}

}
