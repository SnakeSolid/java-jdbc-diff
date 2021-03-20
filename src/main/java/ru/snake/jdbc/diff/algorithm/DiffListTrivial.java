package ru.snake.jdbc.diff.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Trivial algorithm assumes that all rows with the same numbers are equals.
 * Consumes no additional memory.
 *
 * @author snake
 *
 * @param <T>
 *            item type
 */
public final class DiffListTrivial<T> implements DiffList<T> {

	private final List<T> left;

	private final List<T> right;

	/**
	 * Create new list difference calculator for given lists.
	 *
	 * @param left
	 *            left list
	 * @param right
	 *            right list
	 */
	public DiffListTrivial(final List<T> left, final List<T> right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public List<DiffListItem<T>> diff() {
		List<DiffListItem<T>> result = new ArrayList<>();
		Iterator<T> leftIterator = left.iterator();
		Iterator<T> rightIterator = right.iterator();

		while (leftIterator.hasNext() && rightIterator.hasNext()) {
			T leftItem = leftIterator.next();
			T rightItem = rightIterator.next();
			DiffListItem<T> item = DiffListItem.both(leftItem, rightItem);

			result.add(item);
		}

		while (leftIterator.hasNext()) {
			T leftItem = leftIterator.next();
			DiffListItem<T> item = DiffListItem.left(leftItem);

			result.add(item);
		}

		while (rightIterator.hasNext()) {
			T rightItem = rightIterator.next();
			DiffListItem<T> item = DiffListItem.right(rightItem);

			result.add(item);
		}

		return result;
	}

	@Override
	public String toString() {
		return "DiffListTrivial [left=" + left + ", right=" + right + "]";
	}

}
