package ru.snake.jdbc.diff.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.action.CloseDialogAction;
import ru.snake.jdbc.diff.config.BlobParserConfig;
import ru.snake.jdbc.diff.config.Configuration;
import ru.snake.jdbc.diff.config.DriverConfig;
import ru.snake.jdbc.diff.listener.ChangeParametersTableListener;
import ru.snake.jdbc.diff.model.ConnectionParametersTableModel;
import ru.snake.jdbc.diff.model.ConnectionSettings;
import ru.snake.jdbc.diff.model.DriverListModel;
import ru.snake.jdbc.diff.model.ParserListModel;

/**
 * Connection settings dialog.
 *
 * @author snake
 *
 */
public final class ConnectionDialog extends JDialog implements ActionListener {

	private static final int PREFERRED_HEIGHT = 400;

	private static final int PREFERRED_WIDTH = 800;

	private final Configuration config;

	private DriverListModel leftDriverListModel;

	private ParserListModel leftBlobParserModel;

	private ConnectionParametersTableModel leftParametersModel;

	private DriverListModel rightDriverListModel;

	private ParserListModel rightBlobParsersModel;

	private ConnectionParametersTableModel rightParametersModel;

	private JButton saveButton;

	private JButton cancelButton;

	private ConnectionSettings leftConnection;

	private ConnectionSettings rightConnection;

	/**
	 * Create new connection selection dialog.
	 *
	 * @param parent
	 *            parent frame
	 * @param config
	 *            configuration
	 */
	public ConnectionDialog(final MainFrame parent, final Configuration config) {
		super(parent, "Select Connections...", true);

		this.config = config;
		this.leftConnection = null;
		this.rightConnection = null;

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

		leftDriverListModel = new DriverListModel(config);
		leftBlobParserModel = new ParserListModel(config);
		leftParametersModel = new ConnectionParametersTableModel();
		TableColumnModel leftColumnModel = createColumnModel(leftParametersModel);
		leftDriverListModel.addListDataListener(makeParametersListener(leftDriverListModel, leftParametersModel));

		rightDriverListModel = new DriverListModel(config);
		rightBlobParsersModel = new ParserListModel(config);
		rightParametersModel = new ConnectionParametersTableModel();
		TableColumnModel rightColumnModel = createColumnModel(rightParametersModel);
		rightDriverListModel.addListDataListener(makeParametersListener(rightDriverListModel, rightParametersModel));

		Font dialogFont = getFont();
		String fontName = dialogFont.getFontName();
		int fontSize = dialogFont.getSize() + 4;
		Font captionFont = new Font(fontName, Font.BOLD, fontSize);

		JLabel leftSideLabel = new JLabel("Left Data Source");
		leftSideLabel.setFont(captionFont);
		JLabel leftConnectionLabel = new JLabel("Driver:");
		JComboBox<String> leftConnectionList = new JComboBox<>(leftDriverListModel);
		JLabel leftParserLabel = new JLabel("Blob Parser:");
		JComboBox<String> leftParserList = new JComboBox<>(leftBlobParserModel);
		JLabel leftParametersLabel = new JLabel("Driver Parameters:");
		JTable leftParametersTable = new JTable(leftParametersModel, leftColumnModel);
		leftParametersTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		JScrollPane leftParameterScroll = new JScrollPane(leftParametersTable);

		JLabel rightSideLabel = new JLabel("Right Data Source");
		rightSideLabel.setFont(captionFont);
		JLabel rightConnectionLabel = new JLabel("Driver:");
		JComboBox<String> rightConnectionList = new JComboBox<>(rightDriverListModel);
		JLabel rightParserLabel = new JLabel("Blob Parser:");
		JComboBox<String> rightParserList = new JComboBox<>(rightBlobParsersModel);
		JLabel rightParametersLabel = new JLabel("Driver Parameters:");
		JTable rightParametersTable = new JTable(rightParametersModel, rightColumnModel);
		rightParametersTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		JScrollPane rightParameterScroll = new JScrollPane(rightParametersTable);

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
						.addComponent(leftSideLabel)
						.addComponent(leftConnectionLabel)
						.addComponent(leftConnectionList)
						.addComponent(leftParserLabel)
						.addComponent(leftParserList)
						.addComponent(leftParametersLabel)
						.addComponent(leftParameterScroll)
					)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(rightSideLabel)
						.addComponent(rightConnectionLabel)
						.addComponent(rightConnectionList)
						.addComponent(rightParserLabel)
						.addComponent(rightParserList)
						.addComponent(rightParametersLabel)
						.addComponent(rightParameterScroll)
					)
				)
				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
					.addComponent(saveButton)
					.addComponent(cancelButton)
			)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
					.addComponent(leftSideLabel)
					.addComponent(rightSideLabel))
				.addGroup(layout.createParallelGroup()
					.addComponent(leftConnectionLabel)
					.addComponent(rightConnectionLabel))
				.addGroup(layout.createParallelGroup()
					.addComponent(leftConnectionList)
					.addComponent(rightConnectionList))
				.addGroup(layout.createParallelGroup()
					.addComponent(leftParserLabel)
					.addComponent(rightParserLabel))
				.addGroup(layout.createParallelGroup()
					.addComponent(leftParserList)
					.addComponent(rightParserList))
				.addGroup(layout.createParallelGroup()
					.addComponent(leftParametersLabel)
					.addComponent(rightParametersLabel))
				.addGroup(layout.createParallelGroup()
					.addComponent(leftParameterScroll)
					.addComponent(rightParameterScroll))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup()
					.addComponent(saveButton)
					.addComponent(cancelButton))
		);
		// @formatter:on

		layout.linkSize(SwingConstants.HORIZONTAL, saveButton, cancelButton);

		setLayout(layout);
	}

	/**
	 * Make new {@link ChangeParametersTableListener} for given driver list and
	 * parameter models.
	 *
	 * @param driverListModel
	 *            driver list model
	 * @param parametersModel
	 *            driver parameters model
	 * @return new change listener
	 */
	private ChangeParametersTableListener makeParametersListener(
		final DriverListModel driverListModel,
		final ConnectionParametersTableModel parametersModel
	) {
		ChangeParametersTableListener parametersTableListener = new ChangeParametersTableListener(
			config,
			driverListModel,
			parametersModel
		);

		return parametersTableListener;
	}

	/**
	 * Creates new column model for driver property table. Column name will be
	 * created from given table model.
	 *
	 * @param model
	 *            model to get column name from
	 * @return table column model
	 */
	private TableColumnModel createColumnModel(final TableModel model) {
		TableColumn nameColumn = new TableColumn(0);
		nameColumn.setHeaderValue(model.getColumnName(0));

		TableColumn valueColumn = new TableColumn(1);
		valueColumn.setHeaderValue(model.getColumnName(1));

		TableColumnModel columnModel = new DefaultTableColumnModel();
		columnModel.addColumn(nameColumn);
		columnModel.addColumn(valueColumn);

		return columnModel;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		Object source = e.getSource();

		if (source == saveButton) {
			leftConnection = buildConnectionSettings(leftDriverListModel, leftBlobParserModel, leftParametersModel);
			rightConnection = buildConnectionSettings(
				rightDriverListModel,
				rightBlobParsersModel,
				rightParametersModel
			);

			setVisible(false);
		} else if (source == cancelButton) {
			leftConnection = null;
			rightConnection = null;

			setVisible(false);
		}
	}

	private ConnectionSettings buildConnectionSettings(
		final DriverListModel driverListModel,
		final ParserListModel blobMappesModel,
		final ConnectionParametersTableModel parametersModel
	) {
		String driverString = String.valueOf(driverListModel.getSelectedItem());
		String parserString = String.valueOf(blobMappesModel.getSelectedItem());
		DriverConfig driverConfig = config.getDrivers().get(driverString);
		BlobParserConfig parserConfig = config.getBlobParsers().get(parserString);
		Map<String, String> parameters = parametersModel.getParameterMap();
		String url = driverConfig.getUrl();

		for (Entry<String, String> entry : parameters.entrySet()) {
			String parameter = entry.getKey();
			String value = entry.getValue();

			url = url.replace("{" + parameter + "}", value);
		}

		String driverPath = driverConfig.getDriverPath();
		String driverClass = driverConfig.getDriverClass();
		Set<String> binaryTypes = driverConfig.getBinaryTypes();
		String parserLibrary = parserConfig.getLibraryPath();
		String parserClass = parserConfig.getParserClass();
		ConnectionSettings settings = new ConnectionSettings(
			driverPath,
			driverClass,
			binaryTypes,
			parserLibrary,
			parserClass,
			url
		);

		return settings;
	}

	/**
	 * Returns {@code true} if connections selected. Othrwise returns
	 * {@code false}.
	 *
	 * @return true if connections selected
	 */
	public boolean isConnectionsSelected() {
		return leftConnection != null && rightConnection != null;
	}

	/**
	 * @return the leftConnection
	 */
	public ConnectionSettings getLeftConnection() {
		return leftConnection;
	}

	/**
	 * @return the rightConnection
	 */
	public ConnectionSettings getRightConnection() {
		return rightConnection;
	}

}
