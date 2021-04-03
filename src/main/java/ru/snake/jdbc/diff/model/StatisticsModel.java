package ru.snake.jdbc.diff.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * TAble model for data set comparison statistics.
 *
 * @author snake
 *
 */
public final class StatisticsModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = new String[] { "Dataset", "Row Count", "Only Left", "Changed Count",
			"Only Right" };

	private final List<String> datasetNames;

	private final List<CompareStatistics> datasetStatistics;

	public StatisticsModel() {
		this.datasetNames = new ArrayList<>();
		this.datasetStatistics = new ArrayList<>();
	}

	@Override
	public String getColumnName(final int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;

		case 1:
		case 2:
		case 3:
		case 4:
			return Integer.class;

		default:
			return null;
		}
	}

	@Override
	public int getRowCount() {
		return datasetNames.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		switch (columnIndex) {
		case 0:
			return datasetNames.get(rowIndex);

		case 1:
			return datasetStatistics.get(rowIndex).getRowCount();

		case 2:
			return datasetStatistics.get(rowIndex).getOnlyLeftRows();

		case 3:
			return datasetStatistics.get(rowIndex).getChangedCount();

		case 4:
			return datasetStatistics.get(rowIndex).getOnlyRightRows();

		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return false;
	}

	/**
	 * Update model according actual data sets.
	 *
	 * @param datasets
	 *            actual data sets
	 */
	public void setStatistics(final List<ComparedDataset> datasets) {
		if (!datasetNames.isEmpty()) {
			fireTableRowsDeleted(0, datasetNames.size() - 1);

			datasetNames.clear();
			datasetStatistics.clear();
		}

		for (ComparedDataset dataset : datasets) {
			datasetNames.add(dataset.getName());
			datasetStatistics.add(dataset.getStatistics());
		}

		if (!datasetNames.isEmpty()) {
			fireTableRowsInserted(0, datasetNames.size() - 1);
		}
	}

}
