package ru.snake.jdbc.diff.component.model;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;

public class DiffTreeTableSelectionModel extends DefaultTreeSelectionModel {

	public DiffTreeTableSelectionModel() {
		getListSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
			}
		});
	}

	public ListSelectionModel getListSelectionModel() {
		return listSelectionModel;
	}

}
