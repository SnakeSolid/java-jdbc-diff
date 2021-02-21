package ru.snake.jdbc.diff.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import ru.snake.jdbc.diff.config.Configuration;

/**
 * Wrapped over default combo box model for connection name list.
 *
 * @author snake
 *
 */
public final class ParserListModel extends DefaultComboBoxModel<String> {

	/**
	 * Create model from configuration.
	 *
	 * @param config
	 *            configuration settings
	 */
	public ParserListModel(final Configuration config) {
		super(getSortedConnections(config));
	}

	/**
	 * Create sorted list of connection names from configuration.
	 *
	 * @param config
	 *            configuration settings
	 * @return vector of names
	 */
	private static Vector<String> getSortedConnections(final Configuration config) {
		List<String> elements = new ArrayList<String>(config.getBlobParsers().keySet());
		Collections.sort(elements);

		return new Vector<>(elements);
	}

	@Override
	public String toString() {
		return "MapperListModel []";
	}

}
