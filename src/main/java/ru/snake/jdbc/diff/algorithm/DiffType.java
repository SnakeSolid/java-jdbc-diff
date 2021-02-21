package ru.snake.jdbc.diff.algorithm;

/**
 * Diff operation type.
 *
 * @author snake
 *
 */
public enum DiffType {

	/**
	 * Object exists in both sides and both side have the same value.
	 */
	BOTH,

	/**
	 * Object exists only in left side.
	 */
	LEFT,

	/**
	 * Object exists only in right side.
	 */
	RIGHT,

	/**
	 * Object exists in both sides, but value differs.
	 */
	UPDATE,

}
