package ru.snake.jdbc.diff.algorithm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;

public class DiffObject {

	private final Object left;

	private final Object right;

	private final BiPredicate<Object, Object> comparator;

	public DiffObject(final Object left, final Object right, final BiPredicate<Object, Object> comparator) {
		this.left = left;
		this.right = right;
		this.comparator = comparator;
	}

	public Object diff() {
		return diffRecursive(left, right, comparator);
	}

	public static Object
			diffRecursive(final Object left, final Object right, final BiPredicate<Object, Object> comparator) {
		if (left == null && right == null) {
			return DiffObjectItem.both(null);
		} else if (left == null) {
			return DiffObjectItem.right(right);
		} else if (right == null) {
			return DiffObjectItem.left(left);
		}

		Class<? extends Object> leftClass = left.getClass();
		Class<? extends Object> rightClass = right.getClass();

		if (Map.class.isAssignableFrom(leftClass) && Map.class.isAssignableFrom(rightClass)) {
			@SuppressWarnings("unchecked")
			Map<Object, Object> leftMap = (Map<Object, Object>) left;
			@SuppressWarnings("unchecked")
			Map<Object, Object> rightMap = (Map<Object, Object>) right;
			Map<Object, Object> resultMap = new LinkedHashMap<>();

			for (Entry<Object, Object> leftEntry : leftMap.entrySet()) {
				Object leftKey = leftEntry.getKey();
				Object leftValue = leftEntry.getValue();

				if (rightMap.containsKey(leftKey)) {
					Object rightValue = rightMap.get(leftKey);

					if (comparator.test(leftValue, rightValue)) {
						resultMap.put(leftKey, DiffObjectItem.both(leftValue));
					} else {
						resultMap.put(leftKey, diffRecursive(leftValue, rightValue, comparator));
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
			List<Object> leftList = (List<Object>) left;
			@SuppressWarnings("unchecked")
			List<Object> rightList = (List<Object>) right;
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
					resultList.add(diffRecursive(diffItem.getLeft(), diffItem.getRight(), comparator));
					break;

				default:
					throw new RuntimeException("Invalid diff type: " + type);
				}
			}

			return resultList;
		} else if (comparator.test(left, right)) {
			return DiffObjectItem.both(left);
		} else {
			return DiffObjectItem.update(left, right);
		}
	}

	@Override
	public String toString() {
		return "DiffObject [left=" + left + ", right=" + right + ", comparator=" + comparator + "]";
	}

}
