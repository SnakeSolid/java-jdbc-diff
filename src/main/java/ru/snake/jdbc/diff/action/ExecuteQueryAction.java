package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
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
public final class ExecuteQueryAction extends AbstractAction
		implements Action, ConnectionListener, ExecutingStateListener {

	private final MainModel model;

	private final Configuration config;

	/**
	 * Create new select connection action.
	 *
	 * @param mainFrame
	 *            main frame
	 * @param config
	 *            configuration
	 */
	public ExecuteQueryAction(final MainFrame mainFrame, final Configuration config) {
		this.model = mainFrame.getModel();
		this.config = config;

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/execute-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/execute-x24.png"));

		putValue(NAME, "Execute");
		putValue(SHORT_DESCRIPTION, "Execute all queries and build dataset");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_E);

		setEnabled(false);

		model.addConnectionListener(this);
		model.addExecutingListener(this);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		Document queryDocument = model.getQueryDocument();
		int queryLength = queryDocument.getLength();

		try {
			String queryText = queryDocument.getText(0, queryLength);
			CompareDatasetsWorker worker = new CompareDatasetsWorker(model, config, queryText);

			model.setExecuting(true, false);
			worker.execute();
		} catch (BadLocationException exception) {
			Message.showError(exception);
		}
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

		setEnabled(leftConnectionPresent && rightConnectionPresent && !queryExecuting);
	}

}
