package ru.snake.jdbc.diff.worker.mapper;

import java.util.List;

/**
 * Wrapper class for both result sets mappers.
 *
 * @author snake
 *
 */
public class Mappers {

	private final List<ColumnMapper> leftMappers;

	private final List<ColumnMapper> rightMappers;

	private final List<String> allColumns;

	public Mappers(
		final List<ColumnMapper> leftMappers,
		final List<ColumnMapper> rightMappers,
		final List<String> allColumns
	) {
		this.leftMappers = leftMappers;
		this.rightMappers = rightMappers;
		this.allColumns = allColumns;
	}

	/**
	 * @return the leftMappers
	 */
	public List<ColumnMapper> getLeftMappers() {
		return leftMappers;
	}

	/**
	 * @return the rightMappers
	 */
	public List<ColumnMapper> getRightMappers() {
		return rightMappers;
	}

	/**
	 * @return the allColumns
	 */
	public List<String> getAllColumns() {
		return allColumns;
	}

	@Override
	public String toString() {
		return "Mappers [leftMappers=" + leftMappers + ", rightMappers=" + rightMappers + ", allColumns=" + allColumns
				+ "]";
	}

}
