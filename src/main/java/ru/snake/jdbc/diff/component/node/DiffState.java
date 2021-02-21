package ru.snake.jdbc.diff.component.node;

public enum DiffState {

	/**
	 * Both values are equals.
	 */
	EQUALS,

	/**
	 * Value changed.
	 */
	CHANGED,

	/**
	 * Value removed.
	 */
	REMOVED,

}
