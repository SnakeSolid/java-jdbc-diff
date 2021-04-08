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
	 * @param success
	 *            complete status
	 */
	void executingStateChanged(MainModel model, boolean executing, boolean success);

}
