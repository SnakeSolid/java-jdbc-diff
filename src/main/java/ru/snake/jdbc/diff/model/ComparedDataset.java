package ru.snake.jdbc.diff.model;

import java.util.List;

public class ComparedDataset {

	private final String name;

	private final List<String> columnNames;

	private final List<List<DataCell>> left;

	private final List<List<DataCell>> right;

	public ComparedDataset(
		final String name,
		final List<String> columnNames,
		final List<List<DataCell>> left,
		final List<List<DataCell>> right
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
	public List<List<DataCell>> getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public List<List<DataCell>> getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "ComparedDataset [name=" + name + ", columnNames=" + columnNames + ", left=" + left + ", right=" + right
				+ "]";
	}

}
