package ru.snake.jdbc.diff.worker.wrapper;

import javax.swing.SwingUtilities;

import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.MainModel;

/**
 * Wrapper for {@link MainModel} to update data set pages from background
 * thread.
 *
 * @author snake
 *
 */
public final class DatasetUpdateWrapper {

	private final MainModel model;

	private boolean changed;

	public DatasetUpdateWrapper(final MainModel model) {
		this.model = model;
		this.changed = false;
	}

	/**
	 * Push next compared data set to model. If it's the first data set will
	 * clear existing data sets in model.
	 *
	 * @param datasets
	 */
	public void pushComparedDataset(final ComparedDataset datasets) {
		if (changed) {
			SwingUtilities.invokeLater(() -> {
				model.pushComparedDataset(datasets);
			});
		} else {
			changed = true;

			SwingUtilities.invokeLater(() -> {
				model.cleanComparedDatasets();
				model.pushComparedDataset(datasets);
			});
		}
	}

	@Override
	public String toString() {
		return "DatasetUpdateWrapper [model=" + model + ", changed=" + changed + "]";
	}

}
