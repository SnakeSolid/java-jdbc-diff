package ru.snake.jdbc.diff.component;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import ru.snake.jdbc.diff.model.DataTableModel;

/**
 * Table with automatically calculated column widths.
 *
 * @author snake
 *
 */
public final class AdjustColumnTable extends JTable {

	private static final int PREFERRED_COLUMN_WIDTH = 640;

	/**
	 * Creates new instance of table.
	 *
	 * @param model
	 *            table model
	 */
	public AdjustColumnTable(final DataTableModel model) {
		super(model);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	@Override
	public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column) {
		TableColumnModel columnModel = getColumnModel();
		TableColumn tableColumn = columnModel.getColumn(column);
		Component component = super.prepareRenderer(renderer, row, column);
		Dimension preferredSize = component.getPreferredSize();
		Dimension intercellSpacing = getIntercellSpacing();
		int rendererWidth = preferredSize.width;
		int maxColumnWidth = Math.max(rendererWidth + intercellSpacing.width, tableColumn.getPreferredWidth());
		int preferredWidth = Math.min(maxColumnWidth, PREFERRED_COLUMN_WIDTH);

		tableColumn.setPreferredWidth(preferredWidth);

		return component;
	}

}
