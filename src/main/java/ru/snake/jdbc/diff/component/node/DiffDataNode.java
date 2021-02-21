package ru.snake.jdbc.diff.component.node;

import java.util.List;

public class DiffDataNode {

	private final String name;

	private final DiffString left;

	private final DiffString right;

	private final List<DiffDataNode> children;

	public DiffDataNode(String name, DiffString left, DiffString right, List<DiffDataNode> children) {
		this.name = name;
		this.left = left;
		this.right = right;
		this.children = children;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the left
	 */
	public DiffString getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public DiffString getRight() {
		return right;
	}

	/**
	 * @return the children
	 */
	public List<DiffDataNode> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		return name; // This string will be used as node name in tree.
	}
}
