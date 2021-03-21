package ru.snake.jdbc.diff.model;

import java.util.List;

/**
 * Represents single difference table row.
 *
 * @author snake
 *
 */
public final class ComparedRow {

	private final List<DataCell> cells;

	private final boolean changed;

	/**
	 * Create new table row from cells and changed state.
	 *
	 * @param cells
	 *            row cells
	 * @param changed
	 *            contains changed cells
	 */
	private ComparedRow(final List<DataCell> cells, final boolean changed) {
		this.cells = cells;
		this.changed = changed;
	}

	/**
	 * Returns {@link DataCell} for given index.
	 *
	 * @param index
	 *            cell index
	 * @return data cell
	 */
	public DataCell getCell(final int index) {
		return cells.get(index);
	}

	/**
	 * Returns number of cells in this row.
	 *
	 * @return cell count
	 */
	public Object getCellCount() {
		return cells.size();
	}

	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	@Override
	public String toString() {
		return "DatasetRow [cells=" + cells + ", changed=" + changed + "]";
	}

	public static ComparedRow create(final List<DataCell> cells) {
		boolean changed = false;

		for (DataCell cell : cells) {
			if (cell.getState() != CellState.VALID) {
				changed = true;

				break;
			}
		}

		return new ComparedRow(cells, changed);
	}

}
