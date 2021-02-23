package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.dialog.ConnectionDialog;
import ru.snake.jdbc.diff.model.ConnectionSettings;
import ru.snake.jdbc.diff.model.MainModel;

/**
 * Show connection settings dialog action.
 *
 * @author snake
 *
 */
public final class SelectConnectionAction extends AbstractAction implements Action {

	private final MainFrame mainFrame;

	private final MainModel model;

	/**
	 * Create new select connection action.
	 *
	 * @param mainFrame
	 *            main frame
	 */
	public SelectConnectionAction(final MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.model = mainFrame.getModel();

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/plug-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/plug-x24.png"));

		putValue(NAME, "Connection...");
		putValue(SHORT_DESCRIPTION, "Select connection to use for build dataset");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F4"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		ConnectionDialog dialog = mainFrame.getConnectionDialog();
		dialog.setVisible(true);

		if (dialog.isConnectionsSelected()) {
			ConnectionSettings leftConnection = dialog.getLeftConnection();
			ConnectionSettings rightConnection = dialog.getRightConnection();

			model.setConnections(leftConnection, rightConnection);
		}
	}

}
