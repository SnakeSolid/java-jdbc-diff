package ru.snake.jdbc.diff.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.action.CloseDialogAction;
import ru.snake.jdbc.diff.config.Configuration;
import ru.snake.jdbc.diff.config.DiffAlgorithm;
import ru.snake.jdbc.diff.model.AlgorithmString;

/**
 * JDBC diff settings dialog.
 *
 * @author snake
 *
 */
public final class SettingsDialog extends JDialog implements ActionListener {

	private static final List<String> ALGORITHM_NAMES = Arrays.asList(
		"Classic: LCS algorithm (requires N^2 memory)",
		"Greedy: take first available pair (requires 2*N compares)",
		"Trivial: compate data rows by row (fast, no memory required)"
	);

	private static final List<DiffAlgorithm> ALGORITHM_VALUES = Arrays
		.asList(DiffAlgorithm.CLASSIC, DiffAlgorithm.GREEDY, DiffAlgorithm.TRIVIAL);

	private final Configuration config;

	private DefaultComboBoxModel<AlgorithmString> diffAlgorithmListModel;

	private JButton saveButton;

	private JButton cancelButton;

	private DiffAlgorithm diffAlgorithm;

	/**
	 * Create new settings dialog.
	 *
	 * @param parent
	 *            parent frame
	 * @param config
	 *            configuration
	 */
	public SettingsDialog(final MainFrame parent, final Configuration config) {
		super(parent, "Settings...", true);

		this.config = config;
		this.diffAlgorithm = null;

		createComponents();

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

		diffAlgorithmListModel = new DefaultComboBoxModel<>();

		for (int index = 0; index < ALGORITHM_NAMES.size(); index += 1) {
			String text = ALGORITHM_NAMES.get(index);
			DiffAlgorithm algorithm = ALGORITHM_VALUES.get(index);

			diffAlgorithmListModel.addElement(new AlgorithmString(text, algorithm));
		}

		Font dialogFont = getFont();
		String fontName = dialogFont.getFontName();
		int fontSize = dialogFont.getSize() + 4;
		Font captionFont = new Font(fontName, Font.BOLD, fontSize);

		JLabel settingsLabel = new JLabel("Settings");
		settingsLabel.setFont(captionFont);
		JLabel diffAlgorithmLabel = new JLabel("Diff algorithm:");
		JComboBox<AlgorithmString> diffAlgorithmList = new JComboBox<>(diffAlgorithmListModel);
		int selectedAlgorithm = ALGORITHM_VALUES.indexOf(config.getDiffAlgorithm());
		diffAlgorithmList.setSelectedIndex(selectedAlgorithm);

		saveButton = new JButton("Save Settings");
		saveButton.addActionListener(this);
		cancelButton = new JButton("Close");
		cancelButton.addActionListener(this);

		CloseDialogAction closeAction = new CloseDialogAction(this);
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = this.getRootPane();

		rootPane.registerKeyboardAction(closeAction, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

		// @formatter:off
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(settingsLabel)
						.addComponent(diffAlgorithmLabel)
						.addComponent(diffAlgorithmList)
					)
				)
				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
					.addComponent(saveButton)
					.addComponent(cancelButton)
			)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
					.addComponent(settingsLabel)
					.addComponent(diffAlgorithmLabel)
					.addComponent(diffAlgorithmList)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup()
					.addComponent(saveButton)
					.addComponent(cancelButton))
		);
		// @formatter:on

		layout.linkSize(SwingConstants.HORIZONTAL, saveButton, cancelButton);

		setLayout(layout);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		Object source = e.getSource();

		if (source == saveButton) {
			Object value = diffAlgorithmListModel.getSelectedItem();

			if (value != null) {
				diffAlgorithm = ((AlgorithmString) value).getAlgorithm();
			}

			setVisible(false);
		} else if (source == cancelButton) {
			diffAlgorithm = null;

			setVisible(false);
		}
	}

	/**
	 * @return the diffAlgorithm
	 */
	public DiffAlgorithm getDiffAlgorithm() {
		return diffAlgorithm;
	}

	/**
	 * Returns {@code true} if diff algorithm selected. Otherwise returns
	 * {@code false}.
	 *
	 * @return true if algorithm selected
	 */
	public boolean isDiffAlgorithmSelected() {
		return diffAlgorithm != null;
	}

}
