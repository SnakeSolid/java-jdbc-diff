package ru.snake.jdbc.diff.component.node;

/**
 * State of {@link DiffString}.
 *
 * @author snake
 *
 */
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
