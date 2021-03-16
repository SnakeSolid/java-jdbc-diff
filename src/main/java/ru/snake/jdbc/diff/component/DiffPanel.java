package ru.snake.jdbc.diff.component;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.component.cell.DataCellEditor;
import ru.snake.jdbc.diff.component.cell.DataCellRenderer;
import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.DataCell;
import ru.snake.jdbc.diff.model.DataTableModel;

public class DiffPanel extends JPanel {

	private static final int DATASET_TABLE_GAP = 6;

	private final MainFrame mainFrame;

	private final ComparedDataset dataset;

	private final JTable leftTable;

	private final JTable rightTable;

	public DiffPanel(final MainFrame mainFrame, final ComparedDataset dataset) {
		this.mainFrame = mainFrame;
		this.dataset = dataset;

		this.leftTable = createDatasetTable(dataset, dataset.getLeft());
		this.rightTable = createDatasetTable(dataset, dataset.getRight());

		JScrollPane leftTableScroll = new JScrollPane(leftTable);
		leftTableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		leftTableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane rightTableScroll = new JScrollPane(rightTable);
		rightTableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rightTableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rightTableScroll.setVerticalScrollBar(leftTableScroll.getVerticalScrollBar());

		GridLayout layout = new GridLayout(1, 2);
		layout.setHgap(DATASET_TABLE_GAP);

		setLayout(layout);
		add(leftTableScroll);
		add(rightTableScroll);
	}

	/**
	 * Returns left table component.
	 *
	 * @return left table
	 */
	public JTable getLeftTable() {
		return leftTable;
	}

	/**
	 * Returns right table component.
	 *
	 * @return right table
	 */
	public JTable getRightTable() {
		return rightTable;
	}

	/**
	 * Creates table component using given data table as data source. Cell
	 * editor will be able to open compare dialog based on data set data.
	 *
	 * @param aDataset
	 *            data set
	 * @param dataTable
	 *            data table
	 * @return prepared table component
	 */
	private JTable createDatasetTable(final ComparedDataset aDataset, final List<List<DataCell>> dataTable) {
		List<String> columnNames = aDataset.getColumnNames();
		DataTableModel tableModel = new DataTableModel(columnNames, dataTable);
		JTable table = new AdjustColumnTable(tableModel);
		table.setDefaultRenderer(DataCell.class, new DataCellRenderer());
		table.setDefaultEditor(DataCell.class, new DataCellEditor(mainFrame, aDataset.getLeft(), aDataset.getRight()));

		return table;
	}

}
