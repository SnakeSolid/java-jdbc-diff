package ru.snake.jdbc.diff.algorithm;

/**
 * Single diff item.
 *
 * @author snake
 *
 * @param <T>
 *            item type
 */
public class DiffObjectItem<T> {

	private final DiffType type;

	private final T left;

	private final T right;

	private DiffObjectItem(final DiffType type, final T left, final T right) {
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
		return "DiffObjectItem [type=" + type + ", left=" + left + ", right=" + right + "]";
	}

	public static <T> DiffObjectItem<T> both(final T value) {
		return new DiffObjectItem<T>(DiffType.BOTH, value, value);
	}

	public static <T> DiffObjectItem<T> left(final T left) {
		return new DiffObjectItem<T>(DiffType.LEFT, left, null);
	}

	public static <T> DiffObjectItem<T> right(final T right) {
		return new DiffObjectItem<T>(DiffType.RIGHT, null, right);
	}

	public static <T> DiffObjectItem<T> update(final T left, final T right) {
		return new DiffObjectItem<T>(DiffType.UPDATE, left, right);
	}

}
