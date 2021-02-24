package ru.snake.jdbc.diff.component.node;

import java.util.List;

/**
 * Single object view node. Contains field name and value pair.
 *
 * @author snake
 *
 */
public final class ObjectNode {

	private final String name;

	private final String value;

	private final List<ObjectNode> children;

	/**
	 * Create new object node.
	 *
	 * @param name
	 *            name
	 * @param value
	 *            value
	 * @param children
	 *            children nodes
	 */
	public ObjectNode(final String name, final String value, final List<ObjectNode> children) {
		this.name = name;
		this.value = value;
		this.children = children;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the Value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the children
	 */
	public List<ObjectNode> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		return name; // This string will be used as node name in tree.
	}
}
