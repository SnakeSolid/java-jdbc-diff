package ru.snake.jdbc.diff.model;

import ru.snake.jdbc.diff.config.DiffAlgorithm;

public final class CompareSettings {

	private final DiffAlgorithm diffAlgorithm;

	private final boolean showStatistics;

	public CompareSettings(final DiffAlgorithm diffAlgorithm, final boolean showStatistics) {
		this.diffAlgorithm = diffAlgorithm;
		this.showStatistics = showStatistics;
	}

	/**
	 * @return the diffAlgorithm
	 */
	public DiffAlgorithm getDiffAlgorithm() {
		return diffAlgorithm;
	}

	/**
	 * @return the showStatistics
	 */
	public boolean isShowStatistics() {
		return showStatistics;
	}

	@Override
	public String toString() {
		return "CompareSettings [diffAlgorithm=" + diffAlgorithm + ", showStatistics=" + showStatistics + "]";
	}

}
