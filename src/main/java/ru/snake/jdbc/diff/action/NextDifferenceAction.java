package ru.snake.jdbc.diff.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.action.util.DifferenceUtil;
import ru.snake.jdbc.diff.component.DiffPanel;

/**
 * Select next row with difference in table.
 *
 * @author snake
 *
 */
public final class NextDifferenceAction extends AbstractAction implements Action, ChangeListener {

	private final MainFrame frame;

	private JTable leftTable;

	private JTable rightTable;

	/**
	 * Create new close frame action.
	 *
	 * @param frame
	 *            main frame
	 */
	public NextDifferenceAction(final MainFrame frame) {
		this.frame = frame;
		this.leftTable = null;
		this.rightTable = null;

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/next-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/next-x24.png"));

		putValue(NAME, "Next difference");
		putValue(SHORT_DESCRIPTION, "Go to next difference in comparison table");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control DOWN"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_N);

		setEnabled(false);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (leftTable != null && rightTable != null) {
			TableModel leftModel = leftTable.getModel();
			TableModel rightModel = rightTable.getModel();
			int nRows = Integer.min(leftModel.getRowCount(), rightModel.getRowCount());

			if (nRows > 0) {
				int index = DifferenceUtil.getSelectedRow(leftTable.getSelectedRow(), rightTable.getSelectedRow(), -1)
						+ 1;
				int nColumns = Integer.min(leftModel.getColumnCount(), rightModel.getColumnCount());

				while (index < nRows) {
					boolean hasDifference = DifferenceUtil.checkDifference(leftModel, index, nColumns)
							| DifferenceUtil.checkDifference(rightModel, index, nColumns);

					if (hasDifference) {
						leftTable.setRowSelectionInterval(index, index);
						rightTable.setRowSelectionInterval(index, index);

						DifferenceUtil.scrollToRow(leftTable, index);
						DifferenceUtil.scrollToRow(rightTable, index);

						break;
					}

					index += 1;
				}
			}
		}
	}

	@Override
	public void stateChanged(final ChangeEvent e) {
		Object source = e.getSource();

		if (source instanceof JTabbedPane) {
			JTabbedPane tabbedPane = (JTabbedPane) source;
			Component component = tabbedPane.getSelectedComponent();

			if (component != null && component instanceof DiffPanel) {
				DiffPanel diffPanel = (DiffPanel) component;

				leftTable = diffPanel.getLeftTable();
				rightTable = diffPanel.getRightTable();
			} else {
				leftTable = null;
				rightTable = null;
			}

			setEnabled(leftTable != null && rightTable != null);
		}
	}

}
