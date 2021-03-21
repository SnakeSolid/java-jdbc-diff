package ru.snake.jdbc.diff.component.cell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.dialog.ObjectCompareDialog;
import ru.snake.jdbc.diff.dialog.ObjectViewDialog;
import ru.snake.jdbc.diff.model.CellState;
import ru.snake.jdbc.diff.model.ComparedTable;
import ru.snake.jdbc.diff.model.DataCell;

/**
 * Cell editor for difference table. This editor will show
 * {@link ObjectViewDialog} or {@link ObjectCompareDialog} depending on values
 * in table.
 *
 * @author snake
 *
 */
public final class DataCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private final MainFrame mainFrame;

	private final ComparedTable left;

	private final ComparedTable right;

	private final JButton button;

	private int row;

	private int column;

	private Object currentValue;

	/**
	 * Create new instance of {@link DataCellEditor} for given data tables.
	 *
	 * @param mainFrame
	 *            main frame
	 * @param leftTable
	 *            left data table
	 * @param rightTable
	 *            right data table
	 */
	public DataCellEditor(final MainFrame mainFrame, final ComparedTable leftTable, final ComparedTable rightTable) {
		this.mainFrame = mainFrame;
		this.left = leftTable;
		this.right = rightTable;
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
			if (currentValue == null) {
				// If current value is null - do not show any dialogs.
			} else if (isCurrentObjectValid()) {
				Object object = ((DataCell) currentValue).getObject();
				ObjectViewDialog dialog = mainFrame.getObjectViewDialog();
				dialog.setViewObject(object);
				dialog.setVisible(true);
			} else {
				DataCell leftCell = left.getRow(row).getCell(column);
				DataCell rightCell = right.getRow(row).getCell(column);
				Object leftObject = leftCell.getObject();
				Object rightObject = rightCell.getObject();

				if (leftObject != null || rightObject != null) {
					ObjectCompareDialog dialog = mainFrame.getObjectCompareDialog();
					dialog.compareObjects(leftObject, rightObject);
					dialog.setVisible(true);
				}
			}

			fireEditingStopped();
		}
	}

	private boolean isCurrentObjectValid() {
		if (currentValue instanceof DataCell) {
			DataCell dataCell = (DataCell) currentValue;

			return dataCell.getState() == CellState.VALID;
		} else {
			return false;
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
		final int aRow,
		final int aColumn
	) {
		this.currentValue = value;
		this.row = aRow;
		this.column = aColumn;

		return button;
	}

}
