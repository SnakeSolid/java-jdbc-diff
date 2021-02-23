package ru.snake.jdbc.diff.component.model;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

/**
 * Adapter {@link JTree} model. Dispatch events from tree to table model.
 *
 * @author snake
 *
 */
public final class DiffTreeTableModelAdapter extends AbstractTableModel {

	private final JTree tree;

	private final DiffAbstractTreeTableModel treeTableModel;

	public DiffTreeTableModelAdapter(final DiffAbstractTreeTableModel treeTableModel, final JTree tree) {
		this.tree = tree;
		this.treeTableModel = treeTableModel;

		tree.addTreeExpansionListener(new TreeExpansionListener() {
			public void treeExpanded(final TreeExpansionEvent event) {
				fireTableDataChanged();
			}

			public void treeCollapsed(final TreeExpansionEvent event) {
				fireTableDataChanged();
			}
		});
	}

	@Override
	public int getColumnCount() {
		return treeTableModel.getColumnCount();
	}

	@Override
	public String getColumnName(final int column) {
		return treeTableModel.getColumnName(column);
	}

	@Override
	public Class<?> getColumnClass(final int column) {
		return treeTableModel.getColumnClass(column);
	}

	@Override
	public int getRowCount() {
		return tree.getRowCount();
	}

	/**
	 * Returns tree node for given row index.
	 *
	 * @param row
	 *            row index
	 * @return tree node
	 */
	protected Object nodeForRow(final int row) {
		TreePath treePath = tree.getPathForRow(row);

		return treePath.getLastPathComponent();
	}

	@Override
	public Object getValueAt(final int row, final int column) {
		return treeTableModel.getValueAt(nodeForRow(row), column);
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return treeTableModel.isCellEditable(nodeForRow(row), column);
	}

	@Override
	public void setValueAt(final Object value, final int row, final int column) {
		treeTableModel.setValueAt(value, nodeForRow(row), column);
	}
}
