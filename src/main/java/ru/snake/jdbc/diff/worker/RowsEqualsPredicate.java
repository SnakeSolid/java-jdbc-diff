package ru.snake.jdbc.diff.worker;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Check that two data table rows are equals. If number of equals fields less
 * than given parameter rows are considered different.
 *
 * @author snake
 *
 */
public final class RowsEqualsPredicate implements BiPredicate<List<TableCell>, List<TableCell>> {

	private final int nEquals;

	public RowsEqualsPredicate(final int nEquals) {
		this.nEquals = nEquals;
	}

	@Override
	public boolean test(final List<TableCell> left, final List<TableCell> right) {
		int n = Integer.min(left.size(), right.size());
		int nValues = 0;

		for (int index = 0; index < n; index += 1) {
			TableCell leftCell = left.get(index);
			TableCell rightCell = right.get(index);

			if (Objects.equals(leftCell.getText(), rightCell.getText())) {
				nValues += 1;

				if (nValues > nEquals) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "RowsEqualsPredicate [nEquals=" + nEquals + "]";
	}

}
