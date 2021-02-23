package ru.snake.jdbc.diff.component.model;

import ru.snake.jdbc.diff.component.node.ObjectNode;

/**
 * Object view tree table model.
 *
 * @author snake
 *
 */
public final class ObjectViewModel extends DiffAbstractTreeTableModel {

	static protected String[] columnNames = { "Name", "Value" };

	static protected Class<?>[] columnTypes = { DiffTreeTableModel.class, String.class };

	/**
	 * Creates new view tree table model.
	 *
	 * @param rootNode
	 *            model root
	 */
	public ObjectViewModel(final ObjectNode rootNode) {
		super(rootNode);
	}

	/**
	 * Returns child node for given parent.
	 *
	 * @param parent
	 *            parent node
	 * @param index
	 *            child index
	 * @return child node
	 */
	public Object getChild(final Object parent, final int index) {
		return ((ObjectNode) parent).getChildren().get(index);
	}

	/**
	 * Returns number of child nodes for given parent.
	 *
	 * @param parent
	 *            parent node
	 * @return number of child nodes
	 */
	public int getChildCount(final Object parent) {
		return ((ObjectNode) parent).getChildren().size();
	}

	/**
	 * Return column count for different model.
	 *
	 * @return column count
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * Returns column name by column index.
	 *
	 * @param column
	 *            column index
	 * @return column name
	 */
	public String getColumnName(final int column) {
		return columnNames[column];
	}

	/**
	 * Returns column class by column index.
	 *
	 * @param column
	 *            column index
	 * @return column class
	 */
	public Class<?> getColumnClass(final int column) {
		return columnTypes[column];
	}

	/**
	 * Return cell value for given column name by tree node.
	 *
	 * @param node
	 *            node
	 * @param column
	 *            column index
	 * @return cell value
	 */
	public Object getValueAt(final Object node, final int column) {
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

	/**
	 * Return true if given cell editable.
	 *
	 * @param node
	 *            node
	 * @param column
	 *            column index
	 * @return true if cell editable
	 */
	public boolean isCellEditable(final Object node, final int column) {
		return column == 0;
	}

	/**
	 * Set given cell value.
	 *
	 * @param aValue
	 *            new value
	 * @param node
	 *            node
	 * @param column
	 *            column index
	 */
	public void setValueAt(final Object aValue, final Object node, final int column) {
	}

}
