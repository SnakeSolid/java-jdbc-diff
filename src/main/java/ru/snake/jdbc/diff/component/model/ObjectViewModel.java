package ru.snake.jdbc.diff.component.model;

import ru.snake.jdbc.diff.component.node.ObjectNode;

/**
 * Object view tree table model.
 *
 * @author snake
 *
 */
public final class ObjectViewModel extends DiffAbstractTreeTableModel {

	private static final String[] COLUMN_NAMES = { "Name", "Value" };

	private static final Class<?>[] COLUMN_TYPES = { DiffTreeTableModel.class, String.class };

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
	@Override
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
	@Override
	public int getChildCount(final Object parent) {
		return ((ObjectNode) parent).getChildren().size();
	}

	/**
	 * Return column count for different model.
	 *
	 * @return column count
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void setValueAt(final Object aValue, final Object node, final int column) {
	}

}
