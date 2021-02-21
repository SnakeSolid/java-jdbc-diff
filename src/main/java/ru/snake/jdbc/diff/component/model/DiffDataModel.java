package ru.snake.jdbc.diff.component.model;

import ru.snake.jdbc.diff.component.node.DiffDataNode;
import ru.snake.jdbc.diff.component.node.DiffString;

public class DiffDataModel extends DiffAbstractTreeTableModel {

	static protected String[] columnNames = { "Field Name", "Left Value", "Right Value" };

	static protected Class<?>[] columnTypes = { DiffTreeTableModel.class, DiffString.class, DiffString.class };

	public DiffDataModel(DiffDataNode rootNode) {
		super(rootNode);
	}

	public Object getChild(Object parent, int index) {
		return ((DiffDataNode) parent).getChildren().get(index);
	}

	public int getChildCount(Object parent) {
		return ((DiffDataNode) parent).getChildren().size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Class<?> getColumnClass(int column) {
		return columnTypes[column];
	}

	public Object getValueAt(Object node, int column) {
		switch (column) {
		case 0:
			return ((DiffDataNode) node).getName();

		case 1:
			return ((DiffDataNode) node).getLeft();

		case 2:
			return ((DiffDataNode) node).getRight();

		default:
			break;
		}

		return null;
	}

	public boolean isCellEditable(Object node, int column) {
		return true;
	}

	public void setValueAt(Object aValue, Object node, int column) {
	}

}
