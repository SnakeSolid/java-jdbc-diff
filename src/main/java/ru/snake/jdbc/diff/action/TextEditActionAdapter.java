package ru.snake.jdbc.diff.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Wrap given {@link Action} and redefine {@link Action#NAME} property. It's
 * required to not change default frame action map. Otherwise text editing
 * actions will not work in other text components.
 *
 * @author snake
 *
 */
public final class TextEditActionAdapter extends AbstractAction implements Action {

	private final Action action;

	/**
	 * Creates new adapter for given action.
	 *
	 * @param action
	 *            action
	 * @param actionName
	 *            new action name
	 */
	public TextEditActionAdapter(final Action action, final String actionName) {
		this.action = action;

		putValue(Action.NAME, actionName);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		action.actionPerformed(e);
	}

}
