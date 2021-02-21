package ru.snake.jdbc.diff.worker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import ru.snake.jdbc.diff.worker.TableCell;

public interface ColumnMapper {

	/**
	 * Convert single {@link ResultSet} column to {@link TableCell}.
	 *
	 * @param resultSet
	 *            result set
	 * @return table cell
	 * @throws SQLException
	 *             if error occurred
	 */
	public TableCell map(ResultSet resultSet) throws SQLException;

}
