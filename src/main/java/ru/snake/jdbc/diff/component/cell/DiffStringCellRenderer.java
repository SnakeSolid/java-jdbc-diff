package ru.snake.jdbc.diff.component.cell;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ru.snake.jdbc.diff.component.node.DiffString;

public class DiffStringCellRenderer extends JLabel implements TableCellRenderer {

	private static final Color CHANGED_COLOR = new Color(0x00fbbd08);

	private static final Color REMOVED_COLOR = new Color(0x00f2711c);

	public DiffStringCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(
		JTable table,
		Object value,
		boolean isSelected,
		boolean hasFocus,
		int row,
		int column
	) {
		DiffString diffString = (DiffString) value;

		setFont(table.getFont());
		setText(diffString.getValue());
		setToolTipText(diffString.getValue());

		if (isSelected) {
			switch (diffString.getState()) {
			case EQUALS:
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
				break;

			case CHANGED:
				setForeground(mixColor(table.getSelectionForeground(), Color.BLACK));
				setBackground(mixColor(table.getSelectionBackground(), CHANGED_COLOR));
				break;

			case REMOVED:
				setForeground(mixColor(table.getSelectionForeground(), Color.WHITE));
				setBackground(mixColor(table.getSelectionBackground(), REMOVED_COLOR));
				break;

			default:
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
				break;
			}
		} else {
			switch (diffString.getState()) {
			case EQUALS:
				setForeground(table.getForeground());
				setBackground(table.getBackground());
				break;

			case CHANGED:
				setForeground(Color.BLACK);
				setBackground(CHANGED_COLOR);
				break;

			case REMOVED:
				setForeground(Color.WHITE);
				setBackground(REMOVED_COLOR);
				break;

			default:
				setForeground(table.getForeground());
				setBackground(table.getBackground());
				break;
			}
		}

		return this;
	}

	private Color mixColor(Color color1, Color color2) {
		int r = (color1.getRed() + color2.getRed()) / 2;
		int g = (color1.getGreen() + color2.getGreen()) / 2;
		int b = (color1.getBlue() + color2.getBlue()) / 2;

		return new Color(r, g, b);
	}

}
