package ru.snake.jdbc.diff.model;

/**
 * Single dataset cell state. This state shows difference between two datasets.
 *
 * @author snake
 *
 */
public enum CellState {

	/**
	 * Cell contains the same data in both tables.
	 */
	VALID,

	/**
	 * Cell contains different data in tables.
	 */
	CHANGED,

	/**
	 * Cell removed in one table.
	 */
	MISSING,

}
