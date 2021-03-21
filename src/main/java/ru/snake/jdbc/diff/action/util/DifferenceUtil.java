package ru.snake.jdbc.diff.action.util;

import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * Contains utility methods from {@link NextDifferenceAction} and
 * {@link PrevDifferenceAction}.
 *
 * @author snake
 *
 */
public final class DifferenceUtil {

	/**
	 * Returns minimal selected row index. If no rows selected returns -1.
	 *
	 * @param leftRow
	 *            left selected row
	 * @param rightRow
	 *            right selected row
	 * @return selected rows index
	 */
	public static int getMinSelectedRow(final int leftRow, final int rightRow) {
		if (leftRow < 0 && rightRow < 0) {
			return -1;
		} else if (leftRow < 0) {
			return rightRow;
		} else if (rightRow < 0) {
			return leftRow;
		} else {
			return Integer.min(leftRow, rightRow);
		}
	}

	/**
	 * Returns maximal selected row index. If no rows selected returns -1.
	 *
	 * @param leftRow
	 *            left selected row
	 * @param rightRow
	 *            right selected row
	 * @return selected rows index
	 */
	public static int getMaxSelectedRow(final int leftRow, final int rightRow) {
		if (leftRow < 0 && rightRow < 0) {
			return -1;
		} else if (leftRow < 0) {
			return rightRow;
		} else if (rightRow < 0) {
			return leftRow;
		} else {
			return Integer.max(leftRow, rightRow);
		}
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
