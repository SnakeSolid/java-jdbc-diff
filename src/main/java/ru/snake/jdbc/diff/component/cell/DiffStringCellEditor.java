package ru.snake.jdbc.diff.component.cell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.component.model.DiffAbstractTreeTableModel;
import ru.snake.jdbc.diff.component.node.DiffString;
import ru.snake.jdbc.diff.dialog.ObjectViewDialog;

public class DiffStringCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final MainFrame mainFrame;

	private final DiffAbstractTreeTableModel treeTableModel;

	private final JButton button;

	private int row;

	private int column;

	private Object currentValue;

	public DiffStringCellEditor(final MainFrame mainFrame, final DiffAbstractTreeTableModel treeTableModel) {
		this.mainFrame = mainFrame;
		this.treeTableModel = treeTableModel;

		this.button = new JButton();
		this.button.addActionListener(this);
		this.button.setBorderPainted(false);
		this.button.setFocusPainted(false);
		this.button.setContentAreaFilled(false);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == button) {
			if (currentValue != null && currentValue instanceof DiffString) {
				Object object = ((DiffString) currentValue).getObject();
				ObjectViewDialog dialog = mainFrame.getObjectViewDialog();
				dialog.setViewObject(object);
				dialog.setVisible(true);
			}

			fireEditingStopped();
		}
	}

	public Object getCellEditorValue() {
		return currentValue;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.currentValue = value;
		this.row = row;
		this.column = column;

		return button;
	}

}
