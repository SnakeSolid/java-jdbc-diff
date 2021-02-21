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

	public DiffTreeTableCellEditor(final JTree tree) {
		this.tree = tree;
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
			MouseEvent me = (MouseEvent) e;

			tree.dispatchEvent(me);
		}

		return false;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

}
