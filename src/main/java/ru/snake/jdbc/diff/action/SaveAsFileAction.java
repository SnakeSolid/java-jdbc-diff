package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.Message;
import ru.snake.jdbc.diff.dialog.FileDialogs;
import ru.snake.jdbc.diff.model.MainModel;
import ru.snake.jdbc.diff.worker.SaveFileWorker;

/**
 * Show open file dialog and start load content worker.
 *
 * @author snake
 *
 */
public final class SaveAsFileAction extends AbstractAction implements Action {

	private final MainFrame frame;

	private final MainModel model;

	/**
	 * Create new open file action.
	 *
	 * @param frame
	 *            main frame
	 */
	public SaveAsFileAction(final MainFrame frame) {
		this.frame = frame;
		this.model = frame.getModel();

		putValue(NAME, "Save as...");
		putValue(SHORT_DESCRIPTION, "Save current editor content to selected file.");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_A);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JFileChooser chooser = frame.getQueryChooser();
		FileDialogs fileDialogs = new FileDialogs(frame, model);
		fileDialogs.showSaveAsDialog(chooser, this::saveContent);
	}

	/**
	 * Save editor content to file.
	 *
	 * @param file
	 *            file
	 */
	private void saveContent(final File file) {
		Document document = model.getQueryDocument();
		int length = document.getLength();

		try {
			String text = document.getText(0, length);
			SaveFileWorker worker = new SaveFileWorker(model, file, text);
			worker.execute();
		} catch (BadLocationException e) {
			Message.showError(e);
		}
	}

}
