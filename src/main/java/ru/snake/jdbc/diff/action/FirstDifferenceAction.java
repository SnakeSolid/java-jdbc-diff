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

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.action.util.DifferenceUtil;
import ru.snake.jdbc.diff.component.DiffPanel;
import ru.snake.jdbc.diff.model.DataTableModel;

/**
 * Select first row with difference in table.
 *
 * @author snake
 *
 */
public final class FirstDifferenceAction extends AbstractAction implements Action, ChangeListener {

	private final MainFrame frame;

	private JTable leftTable;

	private JTable rightTable;

	private DataTableModel leftModel;

	private DataTableModel rightModel;

	/**
	 * Create new close frame action.
	 *
	 * @param frame
	 *            main frame
	 */
	public FirstDifferenceAction(final MainFrame frame) {
		this.frame = frame;
		this.leftTable = null;
		this.rightTable = null;
		this.leftModel = null;
		this.rightModel = null;

		Icon smallIcon = new ImageIcon(ClassLoader.getSystemResource("icons/difference-first-x16.png"));
		Icon largeIcon = new ImageIcon(ClassLoader.getSystemResource("icons/difference-first-x24.png"));

		putValue(NAME, "First difference");
		putValue(SHORT_DESCRIPTION, "Go to first difference in comparison table");
		putValue(SMALL_ICON, smallIcon);
		putValue(LARGE_ICON_KEY, largeIcon);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift UP"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_F);

		setEnabled(false);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (leftTable != null && rightTable != null) {
			int nRows = Integer.min(leftModel.getRowCount(), rightModel.getRowCount());

			if (nRows > 0) {
				int nextIndex = DifferenceUtil
					.getMinSelectedRow(leftModel.getFirstDifference(), rightModel.getFirstDifference());

				if (nextIndex != -1) {
					leftTable.setRowSelectionInterval(nextIndex, nextIndex);
					rightTable.setRowSelectionInterval(nextIndex, nextIndex);

					DifferenceUtil.scrollToRow(leftTable, nextIndex);
					DifferenceUtil.scrollToRow(rightTable, nextIndex);
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
			boolean enable = false;

			if (component != null && component instanceof DiffPanel) {
				DiffPanel diffPanel = (DiffPanel) component;

				leftTable = diffPanel.getLeftTable();
				rightTable = diffPanel.getRightTable();
				leftModel = diffPanel.getLeftModel();
				rightModel = diffPanel.getRightModel();
				enable = true;
			} else {
				leftTable = null;
				rightTable = null;
				leftModel = null;
				rightModel = null;
			}

			setEnabled(enable);
		}
	}

}
