package ru.snake.jdbc.diff.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ComparedTableTest {

	@Test
	public void shouldCreateTableWhenRowsEmpty() {
		List<ComparedRow> rows = Arrays.asList();
		ComparedTable table = ComparedTable.create(rows);

		assertThat(table.getRowCount(), is(0));
	}

	@Test
	public void shouldCreateTableWhenAllRowsValid() {
		List<ComparedRow> rows = Arrays.asList(createRow("a"), createRow("b"), createRow("c"), createRow("d"));
		ComparedTable table = ComparedTable.create(rows);

		assertThat(table.getRowCount(), is(4));
		assertThat(table.getNextChange(0), is(-1));
		assertThat(table.getNextChange(1), is(-1));
		assertThat(table.getNextChange(2), is(-1));
		assertThat(table.getNextChange(3), is(-1));
		assertThat(table.getPrevChange(0), is(-1));
		assertThat(table.getPrevChange(1), is(-1));
		assertThat(table.getPrevChange(2), is(-1));
		assertThat(table.getPrevChange(3), is(-1));
		assertThat(table.getFirstChange(), is(-1));
		assertThat(table.getLastChange(), is(-1));
	}

	@Test
	public void shouldCreateTableWhenRowsMissing() {
		List<ComparedRow> rows = Arrays.asList(createRow("a"), createRow(null), createRow(null), createRow("d"));
		ComparedTable table = ComparedTable.create(rows);

		assertThat(table.getRowCount(), is(4));
		assertThat(table.getNextChange(0), is(1));
		assertThat(table.getNextChange(1), is(2));
		assertThat(table.getNextChange(2), is(-1));
		assertThat(table.getNextChange(3), is(-1));
		assertThat(table.getPrevChange(0), is(-1));
		assertThat(table.getPrevChange(1), is(-1));
		assertThat(table.getPrevChange(2), is(1));
		assertThat(table.getPrevChange(3), is(2));
		assertThat(table.getFirstChange(), is(1));
		assertThat(table.getLastChange(), is(2));
	}

	@Test
	public void shouldCreateTableWhenFirstRowMissing() {
		List<ComparedRow> rows = Arrays.asList(createRow(null), createRow("b"), createRow("c"), createRow("d"));
		ComparedTable table = ComparedTable.create(rows);

		assertThat(table.getRowCount(), is(4));
		assertThat(table.getNextChange(0), is(-1));
		assertThat(table.getNextChange(1), is(-1));
		assertThat(table.getNextChange(2), is(-1));
		assertThat(table.getNextChange(3), is(-1));
		assertThat(table.getPrevChange(0), is(-1));
		assertThat(table.getPrevChange(1), is(0));
		assertThat(table.getPrevChange(2), is(0));
		assertThat(table.getPrevChange(3), is(0));
		assertThat(table.getFirstChange(), is(0));
		assertThat(table.getLastChange(), is(0));
	}

	@Test
	public void shouldCreateTableWhenLastRowMissing() {
		List<ComparedRow> rows = Arrays.asList(createRow("a"), createRow("b"), createRow("c"), createRow(null));
		ComparedTable table = ComparedTable.create(rows);

		assertThat(table.getRowCount(), is(4));
		assertThat(table.getNextChange(0), is(3));
		assertThat(table.getNextChange(1), is(3));
		assertThat(table.getNextChange(2), is(3));
		assertThat(table.getNextChange(3), is(-1));
		assertThat(table.getPrevChange(0), is(-1));
		assertThat(table.getPrevChange(1), is(-1));
		assertThat(table.getPrevChange(2), is(-1));
		assertThat(table.getPrevChange(3), is(-1));
		assertThat(table.getFirstChange(), is(3));
		assertThat(table.getLastChange(), is(3));
	}

	/**
	 * Creates dummy row from given value.
	 *
	 * @param value
	 *            cell value
	 * @return table row
	 */
	private ComparedRow createRow(final String value) {
		if (value != null) {
			return ComparedRow.create(Arrays.asList(DataCell.valid(value, null)));
		} else {
			return ComparedRow.create(Arrays.asList(DataCell.missing()));
		}
	}

}
