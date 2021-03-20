package ru.snake.jdbc.diff.algorithm;

import java.util.List;

/**
 * Calculate difference between two lists.
 *
 * @author snake
 *
 * @param <T>
 *            item type
 */
public interface DiffList<T> {

	/**
	 * Calculate difference between lists and return new list with
	 * {@link DiffListItem} as difference result.
	 *
	 * @return difference between lists
	 */
	List<DiffListItem<T>> diff();

}
