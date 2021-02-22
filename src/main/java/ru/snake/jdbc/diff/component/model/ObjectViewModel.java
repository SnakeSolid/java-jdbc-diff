package ru.snake.jdbc.diff.component.model;

import ru.snake.jdbc.diff.component.node.ObjectNode;

public class ObjectViewModel extends DiffAbstractTreeTableModel {

	static protected String[] columnNames = { "Name", "Value" };

	static protected Class<?>[] columnTypes = { DiffTreeTableModel.class, String.class };

	public ObjectViewModel(ObjectNode rootNode) {
		super(rootNode);
	}

	public Object getChild(Object parent, int index) {
		return ((ObjectNode) parent).getChildren().get(index);
	}

	public int getChildCount(Object parent) {
		return ((ObjectNode) parent).getChildren().size();
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
			return ((ObjectNode) node).getName();

		case 1:
			return ((ObjectNode) node).getValue();

		default:
			break;
		}

		return null;
	}

	public boolean isCellEditable(Object node, int column) {
		return column == 0;
	}

	public void setValueAt(Object aValue, Object node, int column) {
	}

}
