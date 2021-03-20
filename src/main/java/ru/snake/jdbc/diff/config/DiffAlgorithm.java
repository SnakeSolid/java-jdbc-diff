package ru.snake.jdbc.diff.config;

public enum DiffAlgorithm {

	/**
	 * Classical most longest subsequence algorithm.
	 */
	CLASSIC,

	/**
	 * Memory efficient greedy algorithm. Compares only two head rows.
	 */
	GREEDY,

	/**
	 * All rows equals except for trailing.
	 */
	TRIVIAL,

}
