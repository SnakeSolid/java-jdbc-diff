package ru.snake.jdbc.diff.component.model;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/**
 * Base model class for tree tables.
 *
 * @author snake
 *
 */
public abstract class DiffAbstractTreeTableModel implements DiffTreeTableModel {

	private static final int CHANGED = 0;

	private static final int INSERTED = 1;

	private static final int REMOVED = 2;

	private static final int STRUCTURE_CHANGED = 3;

	protected final Object root;

	protected final EventListenerList listenerList = new EventListenerList();

	/**
	 * Creates new model for given root node.
	 *
	 * @param root
	 *            root node
	 */
	public DiffAbstractTreeTableModel(final Object root) {
		this.root = root;
	}

	/**
	 * Returns model root.
	 *
	 * @return root node
	 */
	public Object getRoot() {
		return root;
	}

	/**
	 * Check that given node is leaf node.
	 *
	 * @return true if node is leaf
	 */
	public boolean isLeaf(final Object node) {
		return getChildCount(node) == 0;
	}

	/**
	 * Dummy method for changing tree path.
	 */
	public void valueForPathChanged(final TreePath path, final Object newValue) {
	}

	/**
	 * Returns index of child node in parent.
	 *
	 * @param parent
	 *            parent node
	 * @param child
	 *            child node
	 * @return index of child node in parent
	 */
	public int getIndexOfChild(final Object parent, final Object child) {
		return 0;
	}

	/**
	 * Add new {@link TreeModelListener} to this model.
	 *
	 * @param listener
	 *            listener
	 */
	public void addTreeModelListener(final TreeModelListener listener) {
		listenerList.add(TreeModelListener.class, listener);
	}

	/**
	 * Remove existing {@link TreeModelListener} from this model.
	 *
	 * @param listener
	 *            listener
	 */
	public void removeTreeModelListener(final TreeModelListener listener) {
		listenerList.remove(TreeModelListener.class, listener);
	}

	private void fireTreeNode(
		final int changeType,
		final Object source,
		final Object[] path,
		final int[] childIndices,
		final Object[] children
	) {
		TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				switch (changeType) {
				case CHANGED:
					((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
					break;

				case INSERTED:
					((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
					break;

				case REMOVED:
					((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
					break;

				case STRUCTURE_CHANGED:
					((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
					break;

				default:
					break;
				}
			}
		}
	}

	/**
	 * Fires node changed event to all listeners.
	 *
	 * @param source
	 *            event source
	 * @param path
	 *            tree path
	 * @param childIndices
	 *            child indexes
	 * @param children
	 *            children nodes
	 */
	protected void fireTreeNodesChanged(
		final Object source,
		final Object[] path,
		final int[] childIndices,
		final Object[] children
	) {
		fireTreeNode(CHANGED, source, path, childIndices, children);
	}

	/**
	 * Fires node inserted event to all listeners.
	 *
	 * @param source
	 *            event source
	 * @param path
	 *            tree path
	 * @param childIndices
	 *            child indexes
	 * @param children
	 *            children nodes
	 */
	protected void fireTreeNodesInserted(
		final Object source,
		final Object[] path,
		final int[] childIndices,
		final Object[] children
	) {
		fireTreeNode(INSERTED, source, path, childIndices, children);
	}

	/**
	 * Fires node removed event to all listeners.
	 *
	 * @param source
	 *            event source
	 * @param path
	 *            tree path
	 * @param childIndices
	 *            child indexes
	 * @param children
	 *            children nodes
	 */
	protected void fireTreeNodesRemoved(
		final Object source,
		final Object[] path,
		final int[] childIndices,
		final Object[] children
	) {
		fireTreeNode(REMOVED, source, path, childIndices, children);
	}

	/**
	 * Fires tree structure event to all listeners.
	 *
	 * @param source
	 *            event source
	 * @param path
	 *            tree path
	 * @param childIndices
	 *            child indexes
	 * @param children
	 *            children nodes
	 */
	protected void fireTreeStructureChanged(
		final Object source,
		final Object[] path,
		final int[] childIndices,
		final Object[] children
	) {
		fireTreeNode(STRUCTURE_CHANGED, source, path, childIndices, children);
	}

}
