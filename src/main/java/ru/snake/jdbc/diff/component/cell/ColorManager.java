package ru.snake.jdbc.diff.component.cell;

import java.awt.Color;

import ru.snake.jdbc.diff.model.CellState;

public class ColorManager {

	private static final Color VALID_COLOR = new Color(0x00ffffff);

	private static final Color CHANGED_COLOR = new Color(0x00fbbd08);

	private static final Color MISSING_COLOR = new Color(0x00f2711c);

	private static final Color EMPTY_COLOR = new Color(0x00cccccc);

	private static final Color EDIT_COLOR = new Color(0x00aaaaaa);

	public static Color getEditColor() {
		return EDIT_COLOR;
	}

	public static Color getForegroundColor(CellState state) {
		switch (state) {
		case VALID:
		case CHANGED:
			return Color.BLACK;

		case MISSING:
			return Color.WHITE;

		default:
			return Color.BLACK;
		}
	}

	public static Color getBackgroundColor(CellState state) {
		switch (state) {
		case VALID:
			return VALID_COLOR;

		case CHANGED:
			return CHANGED_COLOR;

		case MISSING:
			return MISSING_COLOR;

		default:
			return Color.WHITE;
		}
	}

	public static Color getEmptyColor() {
		return EMPTY_COLOR;
	}

	public static Color mixColors(Color color1, Color color2) {
		int r = (color1.getRed() + color2.getRed()) / 2;
		int g = (color1.getGreen() + color2.getGreen()) / 2;
		int b = (color1.getBlue() + color2.getBlue()) / 2;

		return new Color(r, g, b);
	}

}
