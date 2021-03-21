package ru.snake.jdbc.diff.model;

import java.util.Arrays;
import java.util.List;

/**
 * Represents full data set table with changed row offsets.
 *
 * @author snake
 *
 */
public final class ComparedTable {

	private final List<ComparedRow> rows;

	private final int[] nextChange;

	private final int[] prevChange;

	private final int firstChange;

	private final int lastChange;

	/**
	 * Creates new compared table based on rows and changes indexes.
	 *
	 * @param rows
	 *            table rows
	 * @param nextChange
	 *            next change array
	 * @param prevChange
	 *            previous change array
	 * @param firstChange
	 *            first changed row
	 * @param lastChange
	 *            last changed row
	 */
	public ComparedTable(
		final List<ComparedRow> rows,
		final int[] nextChange,
		final int[] prevChange,
		final int firstChange,
		final int lastChange
	) {
		this.rows = rows;
		this.nextChange = nextChange;
		this.prevChange = prevChange;
		this.firstChange = firstChange;
		this.lastChange = lastChange;
	}

	/**
	 * Returns number of rows in table.
	 *
	 * @return row count
	 */
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * Returns table rows for given index.
	 *
	 * @param index
	 *            row index
	 * @return table row
	 */
	public ComparedRow getRow(final int index) {
		return rows.get(index);
	}

	/**
	 * @return the firstChange
	 */
	public int getFirstChange() {
		return firstChange;
	}

	/**
	 * @return the lastChange
	 */
	public int getLastChange() {
		return lastChange;
	}

	/**
	 * Returns next changed row starting from given index.
	 *
	 * @param index
	 *            row index
	 * @return changed row index
	 */
	public int getNextChange(final int index) {
		return nextChange[index];
	}

	/**
	 * Returns previous changed row starting from given index.
	 *
	 * @param index
	 *            row index
	 * @return changed row index
	 */
	public int getPrevChange(final int index) {
		return prevChange[index];
	}

	@Override
	public String toString() {
		return "ComparedTable [rows=" + rows + ", nextDifference=" + Arrays.toString(nextChange) + ", prevDifference="
				+ Arrays.toString(prevChange) + "]";
	}

	/**
	 * Creates new compared table instance and calculates difference row
	 * offsets.
	 *
	 * @param rows
	 *            table rows
	 * @return table instance
	 */
	public static ComparedTable create(final List<ComparedRow> rows) {
		int nRows = rows.size();
		int[] nextChange = new int[nRows];
		int[] prevChange = new int[nRows];
		int firstChange = -1;
		int lastChange = -1;
		int changedRow = -1;

		for (int index = nRows - 1; index >= 0; index -= 1) {
			nextChange[index] = changedRow;

			if (rows.get(index).isChanged()) {
				changedRow = index;
			}
		}

		changedRow = -1;

		for (int index = 0; index < nRows; index += 1) {
			prevChange[index] = changedRow;

			if (rows.get(index).isChanged()) {
				changedRow = index;
			}
		}

		for (int index = 0; index < nRows; index += 1) {
			if (rows.get(index).isChanged()) {
				firstChange = index;

				break;
			}
		}

		for (int index = nRows - 1; index >= 0; index -= 1) {
			if (rows.get(index).isChanged()) {
				lastChange = index;

				break;
			}
		}

		return new ComparedTable(rows, nextChange, prevChange, firstChange, lastChange);
	}

}
