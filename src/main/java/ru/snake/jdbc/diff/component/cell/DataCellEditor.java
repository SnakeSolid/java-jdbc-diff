package ru.snake.jdbc.diff.component.cell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.dialog.ObjectCompareDialog;
import ru.snake.jdbc.diff.model.DataCell;

public class DataCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final MainFrame mainFrame;

	private final List<List<DataCell>> left;

	private final List<List<DataCell>> right;

	private final JButton button;

	private int row;

	private int column;

	private Object currentValue;

	public DataCellEditor(
		final MainFrame mainFrame,
		final List<List<DataCell>> left,
		final List<List<DataCell>> right
	) {
		this.mainFrame = mainFrame;
		this.left = left;
		this.right = right;
		this.button = new JButton();
		this.button.addActionListener(this);
		this.button.setBorderPainted(false);
		this.button.setFocusPainted(false);
		this.button.setContentAreaFilled(false);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == button) {
			DataCell leftCell = left.get(row).get(column);
			DataCell rightCell = right.get(row).get(column);
			Object leftObject = leftCell.getObject();
			Object rightObject = rightCell.getObject();

			if (leftObject != null || rightObject != null) {
				ObjectCompareDialog dialog = mainFrame.getObjectCompareDialog();
				dialog.compareObjects(leftObject, rightObject);
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
