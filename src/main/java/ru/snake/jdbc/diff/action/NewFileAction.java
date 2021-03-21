package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
public final class NewFileAction extends AbstractAction implements Action {

	private final MainFrame frame;

	private final MainModel model;

	/**
	 * Create new open file action.
	 *
	 * @param frame
	 *            main frame
	 */
	public NewFileAction(final MainFrame frame) {
		this.frame = frame;
		this.model = frame.getModel();

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/file-new-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/file-new-x24.png"));

		putValue(NAME, "New");
		putValue(SHORT_DESCRIPTION, "Create new empty query in editor.");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_N);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JFileChooser chooser = frame.getQueryChooser();
		FileDialogs fileDialogs = new FileDialogs(frame, model);
		fileDialogs.showChangeDialog(chooser, this::clearContent, this::saveAndClearContent);
	}

	/**
	 * Save editor content then clear editor.
	 *
	 * @param file
	 *            file
	 */
	private void saveAndClearContent(final File file) {
		Document document = model.getQueryDocument();
		int length = document.getLength();

		try {
			String text = document.getText(0, length);
			SaveFileWorker worker = new SaveFileWorker(model, file, text, this::clearContent);
			worker.execute();
		} catch (BadLocationException e) {
			Message.showError(e);
		}
	}

	/**
	 * Clear query editor content.
	 */
	private void clearContent() {
		try {
			model.setQueryStateNew();
		} catch (BadLocationException exception) {
			Message.showError(exception);
		}
	}

}
