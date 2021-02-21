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
import ru.snake.jdbc.diff.model.listener.EditorStateListener;
import ru.snake.jdbc.diff.worker.SaveFileWorker;

/**
 * Show open file dialog and start load content worker.
 *
 * @author snake
 *
 */
public final class SaveFileAction extends AbstractAction implements Action, EditorStateListener {

	private final MainFrame frame;

	private final MainModel model;

	/**
	 * Create new open file action.
	 *
	 * @param frame
	 *            main frame
	 */
	public SaveFileAction(final MainFrame frame) {
		this.frame = frame;
		this.model = frame.getModel();

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/save-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/save-x24.png"));

		putValue(NAME, "Save");
		putValue(SHORT_DESCRIPTION, "Save current editor content.");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_S);

		setEnabled(false);
		model.addEditorStateListener(this);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		JFileChooser chooser = frame.getQueryChooser();
		FileDialogs fileDialogs = new FileDialogs(frame, model);
		fileDialogs.showSaveDialog(chooser, this::saveContent);
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

	@Override
	public void editorStateChanged(final MainModel otherModel, final boolean modified, final File file) {
		if (model == otherModel) {
			setEnabled(modified);
		}
	}

}
