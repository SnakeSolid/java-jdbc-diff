package ru.snake.jdbc.diff.component.cell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.component.node.DiffString;
import ru.snake.jdbc.diff.dialog.ObjectViewDialog;

/**
 * Cell editor for difference table. This editor will show
 * {@link ObjectViewDialog} for selected value in table.
 *
 * @author snake
 *
 */
public final class DiffStringCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final MainFrame mainFrame;

	private final JButton button;

	private Object currentValue;

	public DiffStringCellEditor(final MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		this.button = new JButton();
		this.button.addActionListener(this);
		this.button.setBackground(ColorManager.getEditColor());
		this.button.setBorderPainted(false);
		this.button.setContentAreaFilled(false);
		this.button.setFocusPainted(false);
		this.button.setOpaque(true);
	}

	public void actionPerformed(final ActionEvent e) {
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

	@Override
	public Object getCellEditorValue() {
		return currentValue;
	}

	@Override
	public Component getTableCellEditorComponent(
		final JTable table,
		final Object value,
		final boolean isSelected,
		final int row,
		final int column
	) {
		this.currentValue = value;

		return button;
	}

}
