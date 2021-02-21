package ru.snake.jdbc.diff.model.listener;

import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.MainModel;

public interface ComparedDatasetListener {

	/**
	 * Called if compared datasets in model was cleaned.
	 *
	 * @param model
	 *            calling model
	 */
	public void comparedDatasetsCleared(final MainModel model);

	/**
	 * Called if new compared dataset was added to model.
	 *
	 * @param model
	 *            calling model
	 * @param dataset
	 *            pushed dataset
	 */
	public void comparedDatasetPushed(final MainModel model, final ComparedDataset dataset);

}
