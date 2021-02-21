package ru.snake.jdbc.diff.component.cell;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class DiffStringCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final String EDIT_COMMAND = "edit";

	private final JButton button;

	private final JColorChooser colorChooser;

	private final JDialog dialog;

	private Object currentValue;

	public DiffStringCellEditor() {
		this.button = new JButton();
		this.button.setActionCommand(EDIT_COMMAND);
		this.button.addActionListener(this);
		this.button.setBorderPainted(false);
		this.colorChooser = new JColorChooser();
		this.dialog = JColorChooser.createDialog(button, "Pick a Color", true, colorChooser, this, null);
	}

	public void actionPerformed(ActionEvent e) {
		if (EDIT_COMMAND.equals(e.getActionCommand())) {
			button.setBackground(Color.BLACK);
			colorChooser.setColor(Color.WHITE);
			dialog.setVisible(true);

			fireEditingStopped();
		} else {
			currentValue = colorChooser.getColor();
		}
	}

	public Object getCellEditorValue() {
		return currentValue;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.currentValue = value;

		return button;
	}

}
