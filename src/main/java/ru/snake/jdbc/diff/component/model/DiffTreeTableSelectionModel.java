package ru.snake.jdbc.diff.component.model;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;

/**
 * Selection for difference tree table. Provides default changed listener and
 * list selection model to top level model.
 *
 * @author snake
 *
 */
public final class DiffTreeTableSelectionModel extends DefaultTreeSelectionModel {

	public DiffTreeTableSelectionModel() {
		getListSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent e) {
			}
		});
	}

	/**
	 * Return internal list selection model.
	 *
	 * @return list selection model
	 */
	public ListSelectionModel getListSelectionModel() {
		return listSelectionModel;
	}

}
