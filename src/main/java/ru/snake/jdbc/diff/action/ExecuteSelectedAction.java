package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.Message;
import ru.snake.jdbc.diff.config.Configuration;
import ru.snake.jdbc.diff.model.ConnectionSettings;
import ru.snake.jdbc.diff.model.MainModel;
import ru.snake.jdbc.diff.model.listener.ConnectionListener;
import ru.snake.jdbc.diff.model.listener.ExecutingStateListener;
import ru.snake.jdbc.diff.worker.CompareDatasetsWorker;

/**
 * Execute query action.
 *
 * @author snake
 *
 */
public final class ExecuteSelectedAction extends AbstractAction
		implements Action, CaretListener, ConnectionListener, ExecutingStateListener {

	private final MainModel model;

	private final Configuration config;

	private int selectionStart;

	private int selectionEnd;

	/**
	 * Create new select connection action.
	 *
	 * @param mainFrame
	 *            main frame
	 * @param config
	 *            configuration
	 */
	public ExecuteSelectedAction(final MainFrame mainFrame, final Configuration config) {
		this.model = mainFrame.getModel();
		this.config = config;
		this.selectionStart = 0;
		this.selectionEnd = 0;

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/execute-selected-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/execute-selected-x24.png"));

		putValue(NAME, "Execute selected");
		putValue(SHORT_DESCRIPTION, "Execute selected query and build dataset");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F5"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_X);

		setEnabled(false);

		model.addConnectionListener(this);
		model.addExecutingListener(this);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		Document queryDocument = model.getQueryDocument();

		try {
			String queryText = queryDocument.getText(selectionStart, selectionEnd - selectionStart);
			CompareDatasetsWorker worker = new CompareDatasetsWorker(model, config, queryText);

			model.setExecuting(true, false);
			worker.execute();
		} catch (BadLocationException exception) {
			Message.showError(exception);
		}
	}

	@Override
	public void caretUpdate(final CaretEvent e) {
		int dot = e.getDot();
		int mark = e.getMark();

		selectionStart = Integer.min(dot, mark);
		selectionEnd = Integer.max(dot, mark);

		updateState();
	}

	@Override
	public void
			connectionsChanged(final MainModel aModel, final ConnectionSettings left, final ConnectionSettings right) {
		if (model == aModel) {
			updateState();
		}
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
		boolean leftConnectionPresent = model.getLeftConnection() != null;
		boolean rightConnectionPresent = model.getRightConnection() != null;
		boolean queryExecuting = model.isExecuting();
		boolean selectionValid = selectionStart < selectionEnd;

		setEnabled(leftConnectionPresent && rightConnectionPresent && !queryExecuting && selectionValid);
	}

}
