package ru.snake.jdbc.diff.model.listener;

import ru.snake.jdbc.diff.model.ConnectionSettings;
import ru.snake.jdbc.diff.model.MainModel;

/**
 * Listener will be called when connection settings modified.
 *
 * @author snake
 *
 */
@FunctionalInterface
public interface ConnectionListener {

	/**
	 * Called when connection settings changed.
	 *
	 * @param model
	 *            changed model
	 * @param left
	 *            new left connection settings
	 * @param right
	 *            new right connection settings
	 */
	void connectionsChanged(MainModel model, ConnectionSettings left, ConnectionSettings right);

}
