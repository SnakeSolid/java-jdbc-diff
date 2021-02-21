package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 * Dispose dialog action. Disposes given dialog when performed.
 *
 * @author snake
 *
 */
public final class CloseDialogAction extends AbstractAction implements Action {

	private final JDialog dialog;

	/**
	 * Create new close dialog action.
	 *
	 * @param dialog
	 *            dialog to dispose
	 */
	public CloseDialogAction(final JDialog dialog) {
		this.dialog = dialog;

		putValue(NAME, "Close");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		this.dialog.setVisible(false);
		this.dialog.dispose();
	}

}
