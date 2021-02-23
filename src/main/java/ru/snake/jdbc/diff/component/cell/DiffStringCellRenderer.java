package ru.snake.jdbc.diff.component.cell;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import ru.snake.jdbc.diff.component.node.DiffString;

/**
 * Difference value renderer for data table.
 *
 * @author snake
 *
 */
public final class DiffStringCellRenderer extends JLabel implements TableCellRenderer {

	private final Color backgroundColor;

	private final Border selectedBorder;

	private final Border emptyBorder;

	public DiffStringCellRenderer() {
		this.backgroundColor = UIManager.getColor("Table.selectionBackground");
		this.selectedBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
		this.emptyBorder = BorderFactory.createEmptyBorder();

		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(
		final JTable table,
		final Object value,
		final boolean isSelected,
		final boolean hasFocus,
		final int row,
		final int column
	) {
		DiffString diffString = (DiffString) value;
		String textValue = diffString.getValue();
		Color foreground = ColorManager.getForegroundColor(diffString.getState());
		Color background;

		setFont(table.getFont());
		setText(diffString.getValue());
		setToolTipText(diffString.getValue());

		if (textValue != null) {
			background = ColorManager.getBackgroundColor(diffString.getState());
		} else {
			background = ColorManager.getEmptyColor();
		}

		if (isSelected) {
			setForeground(foreground);
			setBackground(ColorManager.mixColors(backgroundColor, background));
		} else {
			setForeground(foreground);
			setBackground(background);
		}

		if (hasFocus && isSelected) {
			setBorder(selectedBorder);
		} else {
			setBorder(emptyBorder);
		}

		return this;
	}

}
