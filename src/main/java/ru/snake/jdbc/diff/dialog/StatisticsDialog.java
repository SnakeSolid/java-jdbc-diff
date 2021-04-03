package ru.snake.jdbc.diff.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.TableColumnModel;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.action.CloseDialogAction;
import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.StatisticsModel;

/**
 * Statistics dialog.
 *
 * @author snake
 *
 */
public final class StatisticsDialog extends JDialog implements ActionListener {

	private static final int PREFERRED_HEIGHT = 400;

	private static final int PREFERRED_WIDTH = 600;

	private static final int COLUMN_NAME_WIDTH = 250;

	private StatisticsModel statisticsModel;

	private JButton closeButton;

	/**
	 * Create statistics dialog.
	 *
	 * @param parent
	 *            parent frame
	 */
	public StatisticsDialog(final MainFrame parent) {
		super(parent, "Statistics...", true);

		createComponents();

		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		pack();
		setLocationRelativeTo(parent);
	}

	/**
	 * Creates dialog components.
	 */
	private void createComponents() {
		GroupLayout layout = new GroupLayout(this.getContentPane());
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		statisticsModel = new StatisticsModel();

		Font dialogFont = getFont();
		String fontName = dialogFont.getFontName();
		int fontSize = dialogFont.getSize() + 4;
		Font captionFont = new Font(fontName, Font.BOLD, fontSize);

		JLabel statisticsLabel = new JLabel("Comparison Statistics");
		statisticsLabel.setFont(captionFont);
		JTable statisticsTable = new JTable(statisticsModel);
		TableColumnModel statisticsColumns = statisticsTable.getColumnModel();
		statisticsColumns.getColumn(0).setMinWidth(COLUMN_NAME_WIDTH);
		JScrollPane statisticsScroll = new JScrollPane(statisticsTable);

		closeButton = new JButton("Close");
		closeButton.addActionListener(this);

		CloseDialogAction closeAction = new CloseDialogAction(this);
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = this.getRootPane();

		rootPane.registerKeyboardAction(closeAction, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

		// @formatter:off
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addComponent(statisticsLabel)
				.addComponent(statisticsScroll)
				.addComponent(closeButton, Alignment.TRAILING)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(statisticsLabel)
				.addComponent(statisticsScroll)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(closeButton)
		);
		// @formatter:on

		setLayout(layout);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		Object source = e.getSource();

		if (source == closeButton) {
			setVisible(false);
		}
	}

	/**
	 * Update model according actual data sets.
	 *
	 * @param datasets
	 *            actual data sets
	 */
	public void setStatistics(final List<ComparedDataset> datasets) {
		statisticsModel.setStatistics(datasets);
	}

}
