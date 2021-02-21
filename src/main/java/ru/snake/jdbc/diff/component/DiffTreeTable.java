package ru.snake.jdbc.diff.component;

import javax.swing.JTable;

import ru.snake.jdbc.diff.component.cell.DiffStringCellEditor;
import ru.snake.jdbc.diff.component.cell.DiffStringCellRenderer;
import ru.snake.jdbc.diff.component.cell.DiffTreeTableCellEditor;
import ru.snake.jdbc.diff.component.cell.DiffTreeTableCellRenderer;
import ru.snake.jdbc.diff.component.model.DiffAbstractTreeTableModel;
import ru.snake.jdbc.diff.component.model.DiffTreeTableModel;
import ru.snake.jdbc.diff.component.model.DiffTreeTableModelAdapter;
import ru.snake.jdbc.diff.component.model.DiffTreeTableSelectionModel;
import ru.snake.jdbc.diff.component.node.DiffString;

public class DiffTreeTable extends JTable {

	public DiffTreeTable(DiffAbstractTreeTableModel model) {
		setDiffTreeTableModel(model);
	}

	public void setDiffTreeTableModel(DiffAbstractTreeTableModel model) {
		DiffTreeTableSelectionModel selectionModel = new DiffTreeTableSelectionModel();
		DiffTreeTableCellRenderer cellRenderer = new DiffTreeTableCellRenderer(this, model);
		cellRenderer.setSelectionModel(selectionModel);

		setModel(new DiffTreeTableModelAdapter(model, cellRenderer));
		setSelectionModel(selectionModel.getListSelectionModel());
		setDefaultRenderer(DiffTreeTableModel.class, cellRenderer);
		setDefaultRenderer(DiffString.class, new DiffStringCellRenderer());
		setDefaultEditor(DiffTreeTableModel.class, new DiffTreeTableCellEditor(cellRenderer));
		setDefaultEditor(DiffString.class, new DiffStringCellEditor());
	}
}
