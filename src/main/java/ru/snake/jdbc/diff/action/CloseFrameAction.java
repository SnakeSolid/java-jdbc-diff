package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.Message;
import ru.snake.jdbc.diff.model.MainModel;
import ru.snake.jdbc.diff.worker.SaveFileWorker;

/**
 * Dispose frame action. Disposes given dialog when performed.
 *
 * @author snake
 *
 */
public final class CloseFrameAction extends AbstractAction implements Action {

	private final MainFrame frame;

	private final MainModel model;

	/**
	 * Create new close frame action.
	 *
	 * @param frame
	 *            main frame
	 */
	public CloseFrameAction(final MainFrame frame) {
		this.frame = frame;
		this.model = frame.getModel();

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/exit-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/exit-x24.png"));

		putValue(NAME, "Exit");
		putValue(SHORT_DESCRIPTION, "Exit from application without saving any changes");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_X);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (model.isModified()) {
			int result = JOptionPane.showConfirmDialog(
				frame,
				"Query modified. Do you want to save it?",
				"Content changed",
				JOptionPane.YES_NO_CANCEL_OPTION
			);

			if (result == JOptionPane.YES_OPTION) {
				if (model.hasFile()) {
					saveAndClose(model.getFile());
				} else {
					JFileChooser chooser = frame.getQueryChooser();

					result = chooser.showSaveDialog(frame);

					if (result == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();

						if (file.exists()) {
							result = JOptionPane.showConfirmDialog(
								frame,
								"This file exists. Do you want to overwrite it?",
								"File exists",
								JOptionPane.YES_NO_OPTION
							);

							if (result == JOptionPane.YES_OPTION) {
								saveAndClose(file);
							}
						} else {
							saveAndClose(file);
						}
					}
				}
			} else if (result == JOptionPane.NO_OPTION) {
				closeFrame();
			}
		} else {
			closeFrame();
		}
	}

	/**
	 * Save editor content then clear editor.
	 *
	 * @param file
	 *            file
	 */
	private void saveAndClose(final File file) {
		Document document = model.getQueryDocument();
		int length = document.getLength();

		try {
			String text = document.getText(0, length);
			SaveFileWorker worker = new SaveFileWorker(model, file, text, this::closeFrame);
			worker.execute();
		} catch (BadLocationException e) {
			Message.showError(e);
		}
	}

	/**
	 * Clear query editor content.
	 */
	private void closeFrame() {
		this.frame.dispose();
	}

}
