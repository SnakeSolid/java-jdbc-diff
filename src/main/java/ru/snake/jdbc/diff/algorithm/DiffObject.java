package ru.snake.jdbc.diff.algorithm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;

/**
 * Compares two java objects with known structure. Objects must contain
 * hierarchy as nested {@link Map} and {@link List}. All other values will be
 * compared using given {@link BiPredicate}.
 *
 * @author snake
 *
 */
public final class DiffObject {

	private final Object left;

	private final Object right;

	private final BiPredicate<Object, Object> comparator;

	/**
	 * Creates new difference calculator for given objects.
	 *
	 * @param left
	 *            left object
	 * @param right
	 *            right object
	 * @param comparator
	 *            object comparator
	 */
	public DiffObject(final Object left, final Object right, final BiPredicate<Object, Object> comparator) {
		this.left = left;
		this.right = right;
		this.comparator = comparator;
	}

	/**
	 * Calculate difference between two objects. Returns object with mixed
	 * structure from both source objects all map values, list items and objects
	 * will be replaced with instance {@link DiffObjectItem}.
	 *
	 * @return comparison result
	 */
	public Object diff() {
		return diffRecursive(left, right);
	}

	/**
	 * Compares two objects testing class for both object. If both objects are
	 * {@link Map} then they will be compared using map keys. If both objects
	 * are {@link List} then they will be compared using {@link DiffList}. Other
	 * wise objects will be compared using given comparator.
	 *
	 * @param aLeft
	 *            left object
	 * @param aRight
	 *            right object
	 * @return difference between objects
	 */
	private Object diffRecursive(final Object aLeft, final Object aRight) {
		if (aLeft == null && aRight == null) {
			return DiffObjectItem.both(null);
		} else if (aLeft == null) {
			return DiffObjectItem.right(aRight);
		} else if (aRight == null) {
			return DiffObjectItem.left(aLeft);
		}

		Class<? extends Object> leftClass = aLeft.getClass();
		Class<? extends Object> rightClass = aRight.getClass();

		if (Map.class.isAssignableFrom(leftClass) && Map.class.isAssignableFrom(rightClass)) {
			@SuppressWarnings("unchecked")
			Map<Object, Object> leftMap = (Map<Object, Object>) aLeft;
			@SuppressWarnings("unchecked")
			Map<Object, Object> rightMap = (Map<Object, Object>) aRight;
			Map<Object, Object> resultMap = new LinkedHashMap<>();

			for (Entry<Object, Object> leftEntry : leftMap.entrySet()) {
				Object leftKey = leftEntry.getKey();
				Object leftValue = leftEntry.getValue();

				if (rightMap.containsKey(leftKey)) {
					Object rightValue = rightMap.get(leftKey);

					if (comparator.test(leftValue, rightValue)) {
						resultMap.put(leftKey, DiffObjectItem.both(leftValue));
					} else {
						resultMap.put(leftKey, diffRecursive(leftValue, rightValue));
					}
				} else {
					resultMap.put(leftKey, DiffObjectItem.left(leftValue));
				}
			}

			for (Entry<Object, Object> rightEntry : rightMap.entrySet()) {
				Object rightKey = rightEntry.getKey();
				Object rightValue = rightEntry.getValue();

				if (!leftMap.containsKey(rightKey)) {
					resultMap.put(rightKey, DiffObjectItem.left(rightValue));
				}
			}

			return resultMap;
		} else if (List.class.isAssignableFrom(leftClass) && List.class.isAssignableFrom(rightClass)) {
			@SuppressWarnings("unchecked")
			List<Object> leftList = (List<Object>) aLeft;
			@SuppressWarnings("unchecked")
			List<Object> rightList = (List<Object>) aRight;
			List<DiffListItem<Object>> diffList = new DiffList<Object>(leftList, rightList, comparator).diff();
			List<Object> resultList = new ArrayList<>();

			for (DiffListItem<Object> diffItem : diffList) {
				DiffType type = diffItem.getType();

				switch (type) {
				case BOTH:
					resultList.add(DiffObjectItem.both(diffItem.getLeft()));
					break;

				case LEFT:
					resultList.add(DiffObjectItem.left(diffItem.getLeft()));
					break;

				case RIGHT:
					resultList.add(DiffObjectItem.right(diffItem.getRight()));
					break;

				case UPDATE:
					resultList.add(diffRecursive(diffItem.getLeft(), diffItem.getRight()));
					break;

				default:
					throw new RuntimeException("Invalid diff type: " + type);
				}
			}

			return resultList;
		} else if (comparator.test(aLeft, aRight)) {
			return DiffObjectItem.both(aLeft);
		} else {
			return DiffObjectItem.update(aLeft, aRight);
		}
	}

	@Override
	public String toString() {
		return "DiffObject [left=" + left + ", right=" + right + ", comparator=" + comparator + "]";
	}

}
