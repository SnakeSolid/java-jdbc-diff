package ru.snake.jdbc.diff.model.listener;

import java.io.File;

import ru.snake.jdbc.diff.model.MainModel;

/**
 * Listener for query modification or file change.
 *
 * @author snake
 *
 */
@FunctionalInterface
public interface EditorStateListener {

	/**
	 * Called when query text or file changed.
	 *
	 * @param model
	 *            changed model
	 * @param modified
	 *            is modified
	 * @param file
	 *            file
	 */
	void editorStateChanged(MainModel model, boolean modified, File file);

}
