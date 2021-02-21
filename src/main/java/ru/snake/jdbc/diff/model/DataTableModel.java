package ru.snake.jdbc.diff.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel {

	private final List<String> columnNames;

	private final List<List<DataCell>> dataset;

	public DataTableModel(final List<String> columnNames, final List<List<DataCell>> dataset) {
		this.columnNames = columnNames;
		this.dataset = dataset;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return DataCell.class;
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public int getRowCount() {
		return dataset.size();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		DataCell cell = dataset.get(rowIndex).get(columnIndex);
		CellState state = cell.getState();
		Object object = cell.getObject();

		return state != CellState.VALID && object != null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return dataset.get(rowIndex).get(columnIndex);
	}

}
