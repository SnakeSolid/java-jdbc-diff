package ru.snake.jdbc.diff.model;

import ru.snake.jdbc.diff.config.DiffAlgorithm;

/**
 * Wrapper class to use algorithm names instead of enumeration values in
 * {@link JComboBox}.
 *
 * @author snake
 *
 */
public final class AlgorithmString {

	private final String text;

	private final DiffAlgorithm algorithm;

	/**
	 * Creates new instance of algorithm wrapper.
	 *
	 * @param text
	 *            text
	 * @param algorithm
	 *            algorithm
	 */
	public AlgorithmString(final String text, final DiffAlgorithm algorithm) {
		this.text = text;
		this.algorithm = algorithm;
	}

	/**
	 * @return the algorithm
	 */
	public DiffAlgorithm getAlgorithm() {
		return algorithm;
	}

	@Override
	public String toString() {
		return text;
	}

}
