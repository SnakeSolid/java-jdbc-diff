package ru.snake.jdbc.diff.component.model;

import ru.snake.jdbc.diff.component.node.DiffDataNode;
import ru.snake.jdbc.diff.component.node.DiffString;

/**
 * Object difference tree table model.
 *
 * @author snake
 *
 */
public final class DiffDataModel extends DiffAbstractTreeTableModel {

	static final String[] COLUMN_NAMES = { "Field Name", "Left Value", "Right Value" };

	static final Class<?>[] COLUMN_TYPES = { DiffTreeTableModel.class, DiffString.class, DiffString.class };

	/**
	 * Creates new difference tree table model.
	 *
	 * @param rootNode
	 *            model root
	 */
	public DiffDataModel(final DiffDataNode rootNode) {
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
		return ((DiffDataNode) parent).getChildren().get(index);
	}

	/**
	 * Returns number of child nodes for given parent.
	 *
	 * @param parent
	 *            parent node
	 * @return number of child nodes
	 */
	public int getChildCount(final Object parent) {
		return ((DiffDataNode) parent).getChildren().size();
	}

	/**
	 * Return column count for different model.
	 *
	 * @return column count
	 */
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	/**
	 * Returns column name by column index.
	 *
	 * @param column
	 *            column index
	 * @return column name
	 */
	public String getColumnName(final int column) {
		return COLUMN_NAMES[column];
	}

	/**
	 * Returns column class by column index.
	 *
	 * @param column
	 *            column index
	 * @return column class
	 */
	public Class<?> getColumnClass(final int column) {
		return COLUMN_TYPES[column];
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
		return true;
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
