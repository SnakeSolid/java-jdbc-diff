package ru.snake.jdbc.diff.model;

import java.util.List;

/**
 * Container for data set name, left and right compared tables.
 *
 * @author snake
 *
 */
public final class ComparedDataset {

	private final String name;

	private final List<String> columnNames;

	private final ComparedTable left;

	private final ComparedTable right;

	public ComparedDataset(
		final String name,
		final List<String> columnNames,
		final ComparedTable left,
		final ComparedTable right
	) {
		this.name = name;
		this.columnNames = columnNames;
		this.left = left;
		this.right = right;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the columnNames
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * @return the left
	 */
	public ComparedTable getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public ComparedTable getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "ComparedDataset [name=" + name + ", columnNames=" + columnNames + ", left=" + left + ", right=" + right
				+ "]";
	}

}
