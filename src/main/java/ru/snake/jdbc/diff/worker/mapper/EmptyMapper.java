package ru.snake.jdbc.diff.worker.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import ru.snake.jdbc.diff.worker.TableCell;

public class EmptyMapper implements ColumnMapper {

	public EmptyMapper() {
	}

	@Override
	public TableCell map(ResultSet resultSet) throws SQLException {
		return TableCell.empty();
	}

	@Override
	public String toString() {
		return "EmptyMapper []";
	}

}
