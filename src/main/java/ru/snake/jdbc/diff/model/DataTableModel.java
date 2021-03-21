package ru.snake.jdbc.diff.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public final class DataTableModel extends AbstractTableModel {

	private final List<String> columnNames;

	private final ComparedTable comparedTable;

	public DataTableModel(final List<String> columnNames, final ComparedTable comparedTable) {
		this.columnNames = columnNames;
		this.comparedTable = comparedTable;
	}

	@Override
	public String getColumnName(final int column) {
		return columnNames.get(column);
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return DataCell.class;
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public int getRowCount() {
		return comparedTable.getRowCount();
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		DataCell cell = comparedTable.getRow(rowIndex).getCell(columnIndex);
		CellState state = cell.getState();
		Object object = cell.getObject();

		return state != CellState.MISSING && object != null;
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return comparedTable.getRow(rowIndex).getCell(columnIndex);
	}

	/**
	 * Returns index of next changed row. If index is -1 returns first changed
	 * row.
	 *
	 * @param index
	 *            index
	 * @return next rows index
	 */
	public int getNextDifference(final int index) {
		if (index == -1) {
			return comparedTable.getFirstChange();
		} else {
			return comparedTable.getNextChange(index);
		}
	}

	/**
	 * Returns index of previous changed row. If index is -1 returns last
	 * changed row.
	 *
	 * @param index
	 *            index
	 * @return previous rows index
	 */
	public int getPrevDifference(final int index) {
		if (index == -1) {
			return comparedTable.getLastChange();
		} else {
			return comparedTable.getPrevChange(index);
		}
	}

	/**
	 * Returns index of first changed row or -1 if no changed rows exists.
	 *
	 * @return first rows index
	 */
	public int getFirstDifference() {
		return comparedTable.getFirstChange();
	}

	/**
	 * Returns index of last changed row or -1 if no changed rows exists.
	 *
	 * @return previous rows index
	 */
	public int getLastDifference() {
		return comparedTable.getLastChange();
	}

}
