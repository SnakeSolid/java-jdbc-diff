package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.Document;

import ru.snake.jdbc.diff.TextUndoManager;
import ru.snake.jdbc.diff.listener.DocumentModifiedListener;

public final class RedoAction extends AbstractAction implements Action {

	private final Document document;

	private final TextUndoManager undoManager;

	/**
	 * Creates new undo action for given undo manager.
	 *
	 * @param document
	 *            document
	 * @param undoManager
	 *            undo manager
	 */
	public RedoAction(final Document document, final TextUndoManager undoManager) {
		this.document = document;
		this.undoManager = undoManager;

		setEnabled(false);
		document.addDocumentListener(new DocumentModifiedListener(this::update));
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}

	/**
	 * Set enabled state when document changed.
	 *
	 * @param modifiedDocument
	 *            document
	 */
	private void update(final Document modifiedDocument) {
		if (this.document == modifiedDocument) {
			setEnabled(undoManager.canRedo());
		}
	}

}
