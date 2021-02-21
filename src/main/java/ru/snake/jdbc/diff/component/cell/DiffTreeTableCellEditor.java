package ru.snake.jdbc.diff.component.cell;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;

public class DiffTreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {

	private final JTree tree;

	private final JTable table;

	public DiffTreeTableCellEditor(final JTree tree, final JTable table) {
		this.tree = tree;
		this.table = table;
	}

	public Component getTableCellEditorComponent(
		final JTable table,
		final Object value,
		final boolean isSelected,
		final int r,
		final int c
	) {
		return tree;
	}

	public boolean isCellEditable(EventObject e) {
		if (e instanceof MouseEvent) {
			int colunm1 = 0;
			MouseEvent me = (MouseEvent) e;
			int doubleClick = 2;
			MouseEvent newME = new MouseEvent(
				tree,
				me.getID(),
				me.getWhen(),
				me.getModifiersEx(),
				me.getX() - table.getCellRect(0, colunm1, true).x,
				me.getY(),
				doubleClick,
				me.isPopupTrigger()
			);

			tree.dispatchEvent(newME);
		}

		return false;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

}
