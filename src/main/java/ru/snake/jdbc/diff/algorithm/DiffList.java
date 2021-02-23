package ru.snake.jdbc.diff.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Calculate difference between two lists base on Longest common subsequence
 * algorithm, see problem description
 * {@link https://en.wikipedia.org/wiki/Longest_common_subsequence_problem}.
 * This algorithm requires N^2 memory and compares so it should used only for
 * small lists.
 *
 * @author snake
 *
 * @param <T>
 *            item type
 */
public final class DiffList<T> {

	private final List<T> left;

	private final List<T> right;

	private final BiPredicate<T, T> comparator;

	private final int[][] lcsLengths;

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
	public DiffList(final List<T> left, final List<T> right, final BiPredicate<T, T> comparator) {
		this.left = left;
		this.right = right;
		this.comparator = comparator;
		this.lcsLengths = new int[left.size()][right.size()];
	}

	/**
	 * Calculate difference between lists and return new list with
	 * {@link DiffListItem} as difference result.
	 *
	 * @return difference between lists
	 */
	public List<DiffListItem<T>> diff() {
		fillLcsLengths();

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

		for (int index = types.size() - 1; index >= 0; index -= 1) {
			DiffType type = types.get(index);

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
		int leftIndex = left.size() - 1;
		int rightIndex = right.size() - 1;

		while (leftIndex >= 0 && rightIndex >= 0) {
			if (comparator.test(left.get(leftIndex), right.get(rightIndex))) {
				result.add(DiffType.BOTH);

				leftIndex -= 1;
				rightIndex -= 1;
			} else {
				int leftValue = getMaximalLength(leftIndex - 1, rightIndex);
				int rightValue = getMaximalLength(leftIndex, rightIndex - 1);

				if (leftValue > rightValue) {
					result.add(DiffType.LEFT);

					leftIndex -= 1;
				} else {
					result.add(DiffType.RIGHT);

					rightIndex -= 1;
				}
			}
		}

		while (leftIndex >= 0) {
			result.add(DiffType.LEFT);

			leftIndex -= 1;
		}

		while (rightIndex >= 0) {
			result.add(DiffType.RIGHT);

			rightIndex -= 1;
		}
		return result;
	}

	private void fillLcsLengths() {
		for (int leftIndex = 0; leftIndex < left.size(); leftIndex += 1) {
			for (int rightIndex = 0; rightIndex < right.size(); rightIndex += 1) {
				if (comparator.test(left.get(leftIndex), right.get(rightIndex))) {
					this.lcsLengths[leftIndex][rightIndex] = getMaximalLength(leftIndex - 1, rightIndex - 1) + 1;
				} else {
					this.lcsLengths[leftIndex][rightIndex] = Integer
						.max(getMaximalLength(leftIndex - 1, rightIndex), getMaximalLength(leftIndex, rightIndex - 1));
				}
			}
		}
	}

	/**
	 * Returns maximal length of largest sequence.
	 *
	 * @param i
	 *            left index
	 * @param j
	 *            right index
	 * @return maximal length
	 */
	private int getMaximalLength(final int i, final int j) {
		if (i < 0 || j < 0) {
			return 0;
		}

		return lcsLengths[i][j];
	}

	@Override
	public String toString() {
		return "DiffList [left=" + left + ", right=" + right + ", comparator=" + comparator + ", lcsLengths="
				+ Arrays.toString(lcsLengths) + "]";
	}

}
