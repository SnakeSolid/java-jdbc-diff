package ru.snake.jdbc.diff.model.listener;

import ru.snake.jdbc.diff.model.MainModel;

public interface ExecutingStateListener {

	/**
	 * Called if executing state in model was cleaned.
	 *
	 * @param model
	 *            calling model
	 * @param executing
	 *            new executing state
	 */
	void executingStateChanged(MainModel model, boolean executing);

}
