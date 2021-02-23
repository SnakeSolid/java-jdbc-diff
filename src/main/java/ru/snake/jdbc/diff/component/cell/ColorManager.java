package ru.snake.jdbc.diff.component.cell;

import java.awt.Color;

import ru.snake.jdbc.diff.component.node.DiffState;
import ru.snake.jdbc.diff.model.CellState;

/**
 * Simple container for table cell colors. Used to define background and
 * foreground colors of cell renderers for tables.
 *
 * @author snake
 *
 */
public class ColorManager {

	private static final Color VALID_COLOR = new Color(0x00ffffff);

	private static final Color CHANGED_COLOR = new Color(0x00fbbd08);

	private static final Color MISSING_COLOR = new Color(0x00f2711c);

	private static final Color EMPTY_COLOR = new Color(0x00cccccc);

	private static final Color EDIT_COLOR = new Color(0x00aaaaaa);

	public static Color getEditColor() {
		return EDIT_COLOR;
	}

	/**
	 * Returns foreground color for given {@link CellState}.
	 *
	 * @param state
	 *            cell state
	 * @return foreground color
	 */
	public static Color getForegroundColor(final CellState state) {
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

	/**
	 * Returns foreground color for given {@link DiffState}.
	 *
	 * @param state
	 *            difference state
	 * @return foreground color
	 */
	public static Color getForegroundColor(final DiffState state) {
		switch (state) {
		case EQUALS:
		case CHANGED:
			return Color.BLACK;

		case REMOVED:
			return Color.WHITE;

		default:
			return Color.BLACK;
		}
	}

	/**
	 * Returns background color for given {@link CellState}.
	 *
	 * @param state
	 *            cell state
	 * @return background color
	 */
	public static Color getBackgroundColor(final CellState state) {
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

	/**
	 * Returns background color for given {@link CellState}.
	 *
	 * @param state
	 *            difference state
	 * @return background color
	 */
	public static Color getBackgroundColor(final DiffState state) {
		switch (state) {
		case EQUALS:
			return VALID_COLOR;

		case CHANGED:
			return CHANGED_COLOR;

		case REMOVED:
			return MISSING_COLOR;

		default:
			return Color.WHITE;
		}
	}

	public static Color getEmptyColor() {
		return EMPTY_COLOR;
	}

	/**
	 * Calculate and return average color between two given colors.
	 *
	 * @param color1
	 *            first color
	 * @param color2
	 *            second color
	 * @return mixed color
	 */
	public static Color mixColors(final Color color1, final Color color2) {
		int r = (color1.getRed() + color2.getRed()) / 2;
		int g = (color1.getGreen() + color2.getGreen()) / 2;
		int b = (color1.getBlue() + color2.getBlue()) / 2;

		return new Color(r, g, b);
	}

}
