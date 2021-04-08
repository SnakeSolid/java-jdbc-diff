package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.dialog.StatisticsDialog;
import ru.snake.jdbc.diff.model.MainModel;
import ru.snake.jdbc.diff.model.listener.ExecutingStateListener;

/**
 * Show statistics dialog.
 *
 * @author snake
 *
 */
public final class StatisticsAction extends AbstractAction implements Action, ExecutingStateListener {

	private final MainFrame frame;

	private final MainModel model;

	/**
	 * Create new statistics action.
	 *
	 * @param frame
	 *            main frame
	 */
	public StatisticsAction(final MainFrame frame) {
		this.frame = frame;
		this.model = frame.getModel();

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/statistics-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/statistics-x24.png"));

		putValue(NAME, "Statistics");
		putValue(SHORT_DESCRIPTION, "Show dataset comparison statistics");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_S);

		setEnabled(false);

		model.addExecutingListener(this);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		StatisticsDialog dialog = frame.getStatisticsDialog();

		dialog.setStatistics(model.getComparedDatasets());
		dialog.setVisible(true);
	}

	@Override
	public void executingStateChanged(final MainModel aModel, final boolean executing, final boolean success) {
		if (model == aModel) {
			updateState();
		}
	}

	/**
	 * Update action enabled state.
	 */
	private void updateState() {
		setEnabled(!model.isExecuting());
	}

}
