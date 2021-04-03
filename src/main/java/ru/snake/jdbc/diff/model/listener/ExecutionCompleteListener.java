package ru.snake.jdbc.diff.model.listener;

import ru.snake.jdbc.diff.model.MainModel;

/**
 * Listener called when query execution complate.
 *
 * @author snake
 *
 */
public interface ExecutionCompleteListener {

	/**
	 * Called when query execution complete.
	 *
	 * @param model
	 *            model
	 */
	void executionComplete(MainModel model);

}
