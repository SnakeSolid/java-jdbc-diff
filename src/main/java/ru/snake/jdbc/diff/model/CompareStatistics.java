package ru.snake.jdbc.diff.model;

public final class CompareStatistics {

	private final int rowCount;

	private final int onlyLeftRows;

	private final int changedCount;

	private final int onlyRightRows;

	/**
	 * Create statistics for data set.
	 *
	 * @param rowCount
	 *            number of rows
	 * @param onlyLeftRows
	 *            rows present only in left table
	 * @param changedCount
	 *            number of changed rows
	 * @param onlyRightRows
	 *            rows present only in right table
	 */
	public CompareStatistics(
		final int rowCount,
		final int onlyLeftRows,
		final int changedCount,
		final int onlyRightRows
	) {
		this.rowCount = rowCount;
		this.onlyLeftRows = onlyLeftRows;
		this.changedCount = changedCount;
		this.onlyRightRows = onlyRightRows;
	}

	/**
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * @return the onlyLeftRows
	 */
	public int getOnlyLeftRows() {
		return onlyLeftRows;
	}

	/**
	 * @return the changedCount
	 */
	public int getChangedCount() {
		return changedCount;
	}

	/**
	 * @return the onlyRightRows
	 */
	public int getOnlyRightRows() {
		return onlyRightRows;
	}

	@Override
	public String toString() {
		return "CompareStatistics [rowCount=" + rowCount + ", onlyLeftRows=" + onlyLeftRows + ", changedCount="
				+ changedCount + ", onlyRightRows=" + onlyRightRows + "]";
	}

}
