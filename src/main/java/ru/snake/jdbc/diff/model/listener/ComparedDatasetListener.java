package ru.snake.jdbc.diff.model.listener;

import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.MainModel;

public interface ComparedDatasetListener {

	/**
	 * Called if compared data sets in model was cleaned.
	 *
	 * @param model
	 *            calling model
	 */
	void comparedDatasetsCleared(MainModel model);

	/**
	 * Called if new compared dataset was added to model.
	 *
	 * @param model
	 *            calling model
	 * @param dataset
	 *            pushed data set
	 */
	void comparedDatasetPushed(MainModel model, ComparedDataset dataset);

}
