package ru.snake.jdbc.diff.algorithm;

/**
 * Single difference item.
 *
 * @author snake
 *
 * @param <T>
 *            item type
 */
public final class DiffListItem<T> {

	private final DiffType type;

	private final T left;

	private final T right;

	private DiffListItem(final DiffType type, final T left, final T right) {
		this.type = type;
		this.left = left;
		this.right = right;
	}

	/**
	 * @return the row
	 */
	public DiffType getType() {
		return type;
	}

	/**
	 * @return the left
	 */
	public T getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public T getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "DiffListItem [type=" + type + ", left=" + left + ", right=" + right + "]";
	}

	public static <T> DiffListItem<T> both(final T left, final T right) {
		return new DiffListItem<T>(DiffType.BOTH, left, right);
	}

	public static <T> DiffListItem<T> left(final T left) {
		return new DiffListItem<T>(DiffType.LEFT, left, null);
	}

	public static <T> DiffListItem<T> right(final T right) {
		return new DiffListItem<T>(DiffType.RIGHT, null, right);
	}

}
