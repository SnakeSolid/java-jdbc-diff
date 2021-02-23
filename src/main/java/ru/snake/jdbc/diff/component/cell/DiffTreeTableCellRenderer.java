package ru.snake.jdbc.diff.component.cell;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeModel;

import ru.snake.jdbc.diff.component.DiffTreeTable;

/**
 * Cell renderer for tree cells. This renderer recalculete cellbounds and..
 *
 * @author snake
 *
 */
public final class DiffTreeTableCellRenderer extends JTree implements TableCellRenderer {

	private int visibleRow;

	private DiffTreeTable treeTable;

	public DiffTreeTableCellRenderer(final DiffTreeTable treeTable, final TreeModel model) {
		super(model);

		this.treeTable = treeTable;

		setRowHeight(getRowHeight());
	}

	public void setRowHeight(final int rowHeight) {
		if (rowHeight > 0) {
			super.setRowHeight(rowHeight);

			if (treeTable != null && treeTable.getRowHeight() != rowHeight) {
				treeTable.setRowHeight(getRowHeight());
			}
		}
	}

	public void setBounds(final int x, final int y, final int w, final int h) {
		super.setBounds(x, 0, w, treeTable.getHeight());
	}

	public void paint(final Graphics g) {
		g.translate(0, -visibleRow * getRowHeight());

		super.paint(g);
	}

	public Component getTableCellRendererComponent(
		final JTable table,
		final Object value,
		final boolean isSelected,
		final boolean hasFocus,
		final int row,
		final int column
	) {
		if (isSelected) {
			setForeground(table.getForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}

		visibleRow = row;

		return this;
	}

}
