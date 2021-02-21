package ru.snake.jdbc.diff;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper over
 * {@link JOptionPane#showMessageDialog(java.awt.Component, Object, String, int)}
 * to show error messages.
 *
 * @author snake
 *
 */
public final class Message {

	private static final Logger LOG = LoggerFactory.getLogger(Message.class);

	/**
	 * Show exception message dialog.
	 *
	 * @param e
	 *            exception
	 */
	public static void showError(final Exception e) {
		LOG.warn("Error occurred", e);

		JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), null, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show error message dialog with given message.
	 *
	 * @param message
	 *            error message
	 */
	public static void showError(final String e) {
		JOptionPane.showMessageDialog(null, e, null, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Hide public constructor for utility class.
	 */
	private Message() {
	}

}
