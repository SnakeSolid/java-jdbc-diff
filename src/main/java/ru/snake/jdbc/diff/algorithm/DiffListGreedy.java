package ru.snake.jdbc.diff.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Greedy diff algorithm. Calculates diff base on first matched rows in both
 * lists..
 *
 * @author snake
 *
 * @param <T>
 *            item type
 */
public final class DiffListGreedy<T> implements DiffList<T> {

	private final List<T> left;

	private final List<T> right;

	private final BiPredicate<T, T> comparator;

	/**
	 * Create new list difference calculator for given lists.
	 *
	 * @param left
	 *            left list
	 * @param right
	 *            right list
	 * @param comparator
	 *            object comparator
	 */
	public DiffListGreedy(final List<T> left, final List<T> right, final BiPredicate<T, T> comparator) {
		this.left = left;
		this.right = right;
		this.comparator = comparator;
	}

	@Override
	public List<DiffListItem<T>> diff() {
		List<DiffType> types = buildTypes();
		List<DiffListItem<T>> result = buildItems(types);

		return result;
	}

	/**
	 * Creates comparing result based on source lists and calculated difference.
	 *
	 * @param types
	 *            difference types
	 * @return list of compared items
	 */
	private List<DiffListItem<T>> buildItems(final List<DiffType> types) {
		List<DiffListItem<T>> result = new ArrayList<>();
		Iterator<T> leftIterator = left.iterator();
		Iterator<T> rightIterator = right.iterator();

		for (DiffType type : types) {
			switch (type) {
			case BOTH:
				T leftItem = leftIterator.next();
				T rightItem = rightIterator.next();

				result.add(DiffListItem.both(leftItem, rightItem));
				break;

			case LEFT:
				result.add(DiffListItem.left(leftIterator.next()));
				break;

			case RIGHT:
				result.add(DiffListItem.right(rightIterator.next()));
				break;

			default:
				throw new RuntimeException("Invalid diff type: " + type);
			}
		}

		return result;
	}

	private List<DiffType> buildTypes() {
		List<DiffType> result = new ArrayList<>();
		int leftIndex = 0;
		int rightIndex = 0;

		while (leftIndex < left.size() && rightIndex < right.size()) {
			T leftValue = left.get(leftIndex);
			T rightValue = right.get(rightIndex);

			if (comparator.test(leftValue, rightValue)) {
				result.add(DiffType.BOTH);
			} else {
				int leftOffset = leftIndex;
				int rightOffset = rightIndex;
				boolean success = false;
				boolean leftValid;
				boolean rightValid;

				do {
					leftOffset += 1;
					rightOffset += 1;
					leftValid = leftOffset < left.size();
					rightValid = rightOffset < right.size();

					if (leftValid && comparator.test(left.get(leftOffset), rightValue)) {
						for (int index = leftIndex; index < leftOffset; index += 1) {
							result.add(DiffType.LEFT);
						}

						result.add(DiffType.BOTH);

						leftIndex = leftOffset;
						success = true;

						break;
					}

					if (rightValid && comparator.test(leftValue, right.get(rightOffset))) {
						for (int index = rightIndex; index < rightOffset; index += 1) {
							result.add(DiffType.RIGHT);
						}

						result.add(DiffType.BOTH);

						rightIndex = rightOffset;
						success = true;

						break;
					}
				} while (leftValid & rightValid);

				if (!success) {
					break;
				}
			}

			leftIndex += 1;
			rightIndex += 1;
		}

		while (leftIndex < left.size()) {
			result.add(DiffType.LEFT);

			leftIndex += 1;
		}

		while (rightIndex < right.size()) {
			result.add(DiffType.RIGHT);

			rightIndex += 1;
		}

		return result;
	}

	@Override
	public String toString() {
		return "DiffListGreedy [left=" + left + ", right=" + right + ", comparator=" + comparator + "]";
	}

}
