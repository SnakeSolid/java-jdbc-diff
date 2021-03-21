package ru.snake.jdbc.diff.component;

import java.awt.GridLayout;

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

	private final DataTableModel leftModel;

	private final DataTableModel rightModel;

	private final JTable leftTable;

	private final JTable rightTable;

	public DiffPanel(final MainFrame mainFrame, final ComparedDataset dataset) {
		this.leftModel = new DataTableModel(dataset.getColumnNames(), dataset.getLeft());
		this.rightModel = new DataTableModel(dataset.getColumnNames(), dataset.getRight());
		this.leftTable = createDatasetTable(mainFrame, dataset, leftModel);
		this.rightTable = createDatasetTable(mainFrame, dataset, rightModel);

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
	 * @return the leftModel
	 */
	public DataTableModel getLeftModel() {
		return leftModel;
	}

	/**
	 * @return the rightModel
	 */
	public DataTableModel getRightModel() {
		return rightModel;
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
	 * @param mainFrame
	 *            main frame
	 * @param aDataset
	 *            data set
	 * @param tableModel
	 *            data model
	 * @return prepared table component
	 */
	private JTable createDatasetTable(
		final MainFrame mainFrame,
		final ComparedDataset aDataset,
		final DataTableModel tableModel
	) {
		JTable table = new AdjustColumnTable(tableModel);
		table.setDefaultRenderer(DataCell.class, new DataCellRenderer());
		table.setDefaultEditor(DataCell.class, new DataCellEditor(mainFrame, aDataset.getLeft(), aDataset.getRight()));

		return table;
	}

}
