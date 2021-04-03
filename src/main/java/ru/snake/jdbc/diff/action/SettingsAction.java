package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.dialog.SettingsDialog;
import ru.snake.jdbc.diff.model.CompareSettings;
import ru.snake.jdbc.diff.model.MainModel;

/**
 * Show settings dialog action.
 *
 * @author snake
 *
 */
public final class SettingsAction extends AbstractAction implements Action {

	private final MainFrame mainFrame;

	private final MainModel model;

	/**
	 * Create new select connection action.
	 *
	 * @param mainFrame
	 *            main frame
	 */
	public SettingsAction(final MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.model = mainFrame.getModel();

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/settings-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/settings-x24.png"));

		putValue(NAME, "Settings...");
		putValue(SHORT_DESCRIPTION, "Show application settings dialog.");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F2"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_E);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		SettingsDialog dialog = mainFrame.getSettingsDialog();
		dialog.setVisible(true);

		if (dialog.isCompareSettingsSelected()) {
			CompareSettings compareSettings = dialog.getCompareSettings();

			model.setCompareSettings(compareSettings);
		}
	}

}
