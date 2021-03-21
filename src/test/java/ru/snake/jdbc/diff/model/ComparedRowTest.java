package ru.snake.jdbc.diff.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ComparedRowTest {

	@Test
	public void shouldCreateRowWhenCellsEmpty() {
		List<DataCell> rows = Arrays.asList();
		ComparedRow row = ComparedRow.create(rows);

		assertThat(row.isChanged(), is(false));
		assertThat(row.getCellCount(), is(0));
	}

	@Test
	public void shouldCreateRowWhenAllCellsValid() {
		List<DataCell> rows = Arrays.asList(DataCell.valid("a", null), DataCell.valid("b", null));
		ComparedRow row = ComparedRow.create(rows);

		assertThat(row.isChanged(), is(false));
		assertThat(row.getCellCount(), is(2));
		assertThat(row.getCell(0).getState(), is(CellState.VALID));
		assertThat(row.getCell(1).getState(), is(CellState.VALID));
	}

	@Test
	public void shouldCreateRowWhenOneCellsMissing() {
		List<DataCell> rows = Arrays.asList(DataCell.valid("a", null), DataCell.missing(), DataCell.valid("b", null));
		ComparedRow row = ComparedRow.create(rows);

		assertThat(row.isChanged(), is(true));
		assertThat(row.getCellCount(), is(3));
		assertThat(row.getCell(0).getState(), is(CellState.VALID));
		assertThat(row.getCell(1).getState(), is(CellState.MISSING));
		assertThat(row.getCell(2).getState(), is(CellState.VALID));
	}

	@Test
	public void shouldCreateRowWhenOneCellsChanged() {
		List<DataCell> rows = Arrays
			.asList(DataCell.valid("a", null), DataCell.changed("b", null), DataCell.valid("c", null));
		ComparedRow row = ComparedRow.create(rows);

		assertThat(row.isChanged(), is(true));
		assertThat(row.getCellCount(), is(3));
		assertThat(row.getCell(0).getState(), is(CellState.VALID));
		assertThat(row.getCell(1).getState(), is(CellState.CHANGED));
		assertThat(row.getCell(2).getState(), is(CellState.VALID));
	}

}
