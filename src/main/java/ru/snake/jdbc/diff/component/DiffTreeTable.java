package ru.snake.jdbc.diff.component;

import javax.swing.JTable;
import javax.swing.JTree;

import ru.snake.jdbc.diff.component.cell.DiffTreeTableCellEditor;
import ru.snake.jdbc.diff.component.cell.DiffTreeTableCellRenderer;
import ru.snake.jdbc.diff.component.model.DiffAbstractTreeTableModel;
import ru.snake.jdbc.diff.component.model.DiffTreeTableModel;
import ru.snake.jdbc.diff.component.model.DiffTreeTableModelAdapter;
import ru.snake.jdbc.diff.component.model.DiffTreeTableSelectionModel;

/**
 * Table with {@link JTree} as first column.
 *
 * @author snake
 *
 */
public final class DiffTreeTable extends JTable {

	public DiffTreeTable(final DiffAbstractTreeTableModel model) {
		setDiffTreeTableModel(model);
	}

	public void setDiffTreeTableModel(final DiffAbstractTreeTableModel model) {
		DiffTreeTableSelectionModel selectionModel = new DiffTreeTableSelectionModel();
		DiffTreeTableCellRenderer cellRenderer = new DiffTreeTableCellRenderer(this, model);
		cellRenderer.setSelectionModel(selectionModel);

		setModel(new DiffTreeTableModelAdapter(model, cellRenderer));
		setSelectionModel(selectionModel.getListSelectionModel());
		setDefaultRenderer(DiffTreeTableModel.class, cellRenderer);
		setDefaultEditor(DiffTreeTableModel.class, new DiffTreeTableCellEditor(cellRenderer));
	}
}
