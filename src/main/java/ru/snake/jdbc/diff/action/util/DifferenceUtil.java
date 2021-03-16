package ru.snake.jdbc.diff.action.util;

import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.TableModel;

import ru.snake.jdbc.diff.model.DataCell;

/**
 * Contains utility methods from {@link NextDifferenceAction} and
 * {@link PrevDifferenceAction}.
 *
 * @author snake
 *
 */
public final class DifferenceUtil {

	/**
	 * Returns selected row. If no rows selected returns nRows value.
	 *
	 * @param leftRow
	 *            left selected row
	 * @param rightRow
	 *            right selected row
	 * @param nRows
	 *            number of rows
	 * @return selected rows value
	 */
	public static int getSelectedRow(final int leftRow, final int rightRow, final int nRows) {
		int index = Integer.max(leftRow, rightRow);

		if (index == -1) {
			return nRows;
		} else {
			return index;
		}
	}

	/**
	 * Check that given row of table model has difference in {@link DataCell}'s.
	 *
	 * @param model
	 *            table model
	 * @param index
	 *            row index
	 * @param nColumns
	 *            column count
	 * @return true if row has difference
	 */
	public static boolean checkDifference(final TableModel model, final int index, final int nColumns) {
		for (int column = 0; column < nColumns; column += 1) {
			Object value = model.getValueAt(index, column);

			if (value != null && value instanceof DataCell) {
				DataCell cell = (DataCell) value;

				switch (cell.getState()) {
				case CHANGED:
				case MISSING:
					return true;

				default:
					break;
				}
			}
		}

		return false;
	}

	/**
	 * Scrolls given table to selected row if it's necessarily.
	 *
	 * @param table
	 *            table
	 * @param index
	 *            row index
	 */
	public static void scrollToRow(final JTable table, final int index) {
		Container parent = table.getParent();

		if (parent instanceof JViewport) {
			JViewport viewport = (JViewport) parent;
			Rectangle cellRect = table.getCellRect(index, 0, true);
			Point position = viewport.getViewPosition();

			cellRect.setLocation(cellRect.x - position.x, cellRect.y - position.y);
			viewport.scrollRectToVisible(cellRect);
		}
	}

	private DifferenceUtil() {
		// Hide constructor for utility class.
	}

}
