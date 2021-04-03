package ru.snake.jdbc.diff.model;

import java.util.List;

/**
 * Container for data set name, left and right compared tables.
 *
 * @author snake
 *
 */
public final class ComparedDataset {

	private final String name;

	private final List<String> columnNames;

	private final ComparedTable left;

	private final ComparedTable right;

	private final CompareStatistics statistics;

	/**
	 * Create new database from two tables and row statistics.
	 *
	 * @param name
	 *            data set name
	 * @param columnNames
	 *            all data set columns
	 * @param left
	 *            left table data
	 * @param right
	 *            right table data
	 * @param statistics
	 *            data set statistics
	 */
	private ComparedDataset(
		final String name,
		final List<String> columnNames,
		final ComparedTable left,
		final ComparedTable right,
		final CompareStatistics statistics
	) {
		this.name = name;
		this.columnNames = columnNames;
		this.left = left;
		this.right = right;
		this.statistics = statistics;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the columnNames
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * @return the left
	 */
	public ComparedTable getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public ComparedTable getRight() {
		return right;
	}

	/**
	 * @return the statistics
	 */
	public CompareStatistics getStatistics() {
		return statistics;
	}

	@Override
	public String toString() {
		return "ComparedDataset [name=" + name + ", columnNames=" + columnNames + ", left=" + left + ", right=" + right
				+ ", statistics=" + statistics + "]";
	}

	public static ComparedDataset create(
		final String name,
		final List<String> columnNames,
		final ComparedTable left,
		final ComparedTable right
	) {
		int nRows = Integer.max(left.getRowCount(), right.getRowCount());
		int onlyLeftRows = 0;
		int nChangedRows = 0;
		int onlyRightRows = 0;

		for (int index = 0; index < nRows; index += 1) {
			ComparedRow leftRow = left.getRow(index);
			ComparedRow rightRow = right.getRow(index);

			if (leftRow.isChanged() && rightRow.isChanged()) {
				nChangedRows += 1;
			} else if (!leftRow.isChanged() && rightRow.isChanged()) {
				onlyRightRows += 1;
			} else if (leftRow.isChanged() && !rightRow.isChanged()) {
				onlyLeftRows += 1;
			}
		}

		CompareStatistics statistics = new CompareStatistics(nRows, onlyLeftRows, nChangedRows, onlyRightRows);

		return new ComparedDataset(name, columnNames, left, right, statistics);
	}

}
