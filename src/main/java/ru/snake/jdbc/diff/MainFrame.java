package ru.snake.jdbc.diff;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import ru.snake.jdbc.diff.action.CloseFrameAction;
import ru.snake.jdbc.diff.action.ExecuteQueryAction;
import ru.snake.jdbc.diff.action.NewFileAction;
import ru.snake.jdbc.diff.action.OpenFileAction;
import ru.snake.jdbc.diff.action.RedoAction;
import ru.snake.jdbc.diff.action.SaveAsFileAction;
import ru.snake.jdbc.diff.action.SaveFileAction;
import ru.snake.jdbc.diff.action.SelectConnectionAction;
import ru.snake.jdbc.diff.action.UndoAction;
import ru.snake.jdbc.diff.component.AdjustColumnTable;
import ru.snake.jdbc.diff.component.cell.DataCellEditor;
import ru.snake.jdbc.diff.component.cell.DataCellRenderer;
import ru.snake.jdbc.diff.config.Configuration;
import ru.snake.jdbc.diff.dialog.ConnectionDialog;
import ru.snake.jdbc.diff.dialog.ObjectCompareDialog;
import ru.snake.jdbc.diff.listener.TextEditorMouseListener;
import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.DataCell;
import ru.snake.jdbc.diff.model.DataTableModel;
import ru.snake.jdbc.diff.model.MainModel;
import ru.snake.jdbc.diff.model.listener.ComparedDatasetListener;

/**
 * Main application frame.
 *
 * @author snake
 *
 */
public final class MainFrame extends JFrame implements ComparedDatasetListener {

	private static final int PREFERRED_WIDTH = 800;

	private static final int PREFERRED_HEIGHT = 600;

	private static final float CARET_ASPECT_RATIO = 0.1f;

	private static final int DEFAULT_DIVIDER_LOCATION = 350;

	private final Configuration config;

	private final MainModel model;

	private JFileChooser queryChooser;

	private ConnectionDialog connectionDialog;

	private ObjectCompareDialog objectCompareDialog;

	private Action newFileAction;

	private Action openFileAction;

	private Action saveFileAction;

	private Action saveAsFileAction;

	private CloseFrameAction closeFrameAction;

	private SelectConnectionAction prepareConnectionAction;

	private ExecuteQueryAction executeQueryAction;

	private JTextComponent queryText;

	private JTabbedPane datasetTabs;

	/**
	 * Creates new frame instance with given configuration settings.
	 *
	 * @param title
	 *            frame title
	 * @param config
	 *            configuration settings
	 * @param model
	 *            internal state model
	 */
	public MainFrame(final String title, final Configuration config, final MainModel model) {
		super(title);

		this.config = config;
		this.model = model;

		createActions();
		createMenuBar();
		createToolBar();
		createComponents();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		pack();

		model.addComparedDatasetListener(this);
		queryText.requestFocusInWindow();
	}

	/**
	 * Creates and initialize all actions.
	 */
	private void createActions() {
		queryChooser = null;
		connectionDialog = null;

		newFileAction = new NewFileAction(this);
		openFileAction = new OpenFileAction(this);
		saveFileAction = new SaveFileAction(this);
		saveAsFileAction = new SaveAsFileAction(this);
		closeFrameAction = new CloseFrameAction(this);
		prepareConnectionAction = new SelectConnectionAction(this);
		executeQueryAction = new ExecuteQueryAction(this, config);
	}

	/**
	 * Creates new menu bar for this frame.
	 */
	private void createMenuBar() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		fileMenu.add(newFileAction);
		fileMenu.add(openFileAction);
		fileMenu.add(saveFileAction);
		fileMenu.add(saveAsFileAction);
		fileMenu.addSeparator();
		fileMenu.add(closeFrameAction);

		JMenu connectionMenu = new JMenu("Connection");
		connectionMenu.setMnemonic('C');
		connectionMenu.add(prepareConnectionAction);
		connectionMenu.add(executeQueryAction);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(connectionMenu);

		setJMenuBar(menuBar);
	}

	/**
	 * Creates application tool bar for this frame.
	 */
	private void createToolBar() {
		JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
		toolBar.setFloatable(false);
		toolBar.add(newFileAction);
		toolBar.add(openFileAction);
		toolBar.add(saveFileAction);
		toolBar.addSeparator();
		toolBar.add(prepareConnectionAction);
		toolBar.add(executeQueryAction);

		add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Create all required components on frame.
	 */
	private void createComponents() {
		Font configuredFont = getConfigFont();
		queryText = new JTextPane(this.model.getQueryDocument());
		queryText.putClientProperty("caretAspectRatio", CARET_ASPECT_RATIO);
		queryText.setFont(configuredFont);
		initUndoManager(queryText);
		initPopupMenu(queryText);

		JScrollPane queryScroll = new JScrollPane(queryText);
		queryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		queryScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		datasetTabs = new JTabbedPane();
		datasetTabs.setTabLayoutPolicy(JTabbedPane.TOP);
		datasetTabs.setTabPlacement(JTabbedPane.SCROLL_TAB_LAYOUT);

		JSplitPane workspace = new JSplitPane(JSplitPane.VERTICAL_SPLIT, queryScroll, datasetTabs);
		workspace.setDividerLocation(DEFAULT_DIVIDER_LOCATION);

		add(workspace, BorderLayout.CENTER);
	}

	/**
	 * Creates undo and redo actions for given text component.
	 *
	 * @param textComponent
	 *            text component
	 */
	private void initUndoManager(final JTextComponent textComponent) {
		UndoManager undoManager = new UndoManager();
		TextUndoManager textManager = new TextUndoManager(undoManager);
		Document document = textComponent.getDocument();
		Action undoAction = new UndoAction(document, textManager);
		Action redoAction = new RedoAction(document, textManager);
		document.addUndoableEditListener(textManager);

		InputMap inputMap = textComponent.getInputMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "Undo");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "Redo");

		ActionMap actionMap = textComponent.getActionMap();
		actionMap.put("Undo", undoAction);
		actionMap.put("Redo", redoAction);
	}

	/**
	 * Creates and set context menu (cut, copy, paste and select all actions)
	 * for given component.
	 *
	 * @param textComponent
	 *            text component
	 */
	private void initPopupMenu(final JTextComponent textComponent) {
		ActionMap actionMap = textComponent.getActionMap();
		Action cut = actionMap.get(DefaultEditorKit.cutAction);
		Action copy = actionMap.get(DefaultEditorKit.copyAction);
		Action paste = actionMap.get(DefaultEditorKit.pasteAction);
		Action selectAll = actionMap.get(DefaultEditorKit.selectAllAction);

		cut.putValue(Action.NAME, "Cut");
		copy.putValue(Action.NAME, "Copy");
		paste.putValue(Action.NAME, "Paste");
		selectAll.putValue(Action.NAME, "Select all");

		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(cut);
		popupMenu.add(copy);
		popupMenu.add(paste);
		popupMenu.addSeparator();
		popupMenu.add(selectAll);

		MouseListener popupListener = new TextEditorMouseListener(popupMenu);
		textComponent.addMouseListener(popupListener);
	}

	/**
	 * Creates font for query editor and result area.
	 *
	 * @return font
	 */
	private Font getConfigFont() {
		String fontName = this.config.getFont().getName();
		int fontStyle = this.config.getFont().getStyle().asInt();
		int fontSize = this.config.getFont().getSize();
		Font font = new Font(fontName, fontStyle, fontSize);

		return font;
	}

	/**
	 * Returns lay initialized {@link JFileChooser} for query files.
	 *
	 * @return file chooser
	 */
	public JFileChooser getQueryChooser() {
		if (queryChooser == null) {
			FileFilter filter = new FileNameExtensionFilter("Query files (*.sql; *.txt)", "sql", "txt");
			queryChooser = new JFileChooser();
			queryChooser.addChoosableFileFilter(filter);
			queryChooser.setFileFilter(filter);
		}

		return queryChooser;
	}

	/**
	 * Returns lay initialized {@link ConnectionDialog} to select connection
	 * settings.
	 *
	 * @return connection dialog
	 */
	public ConnectionDialog getConnectionDialog() {
		if (connectionDialog == null) {
			connectionDialog = new ConnectionDialog(this, config);
		}

		return connectionDialog;
	}

	/**
	 * Returns lay initialized {@link ObjectCompareDialog} to select connection
	 * settings.
	 *
	 * @return object compare dialog
	 */
	public ObjectCompareDialog getObjectCompareDialog() {
		if (objectCompareDialog == null) {
			objectCompareDialog = new ObjectCompareDialog(this);
		}

		return objectCompareDialog;
	}

	/**
	 * Returns this frame model.
	 *
	 * @return model
	 */
	public MainModel getModel() {
		return model;
	}

	@Override
	public void comparedDatasetsCleared(MainModel model) {
		datasetTabs.removeAll();
	}

	@Override
	public void comparedDatasetPushed(MainModel model, ComparedDataset dataset) {
		JScrollPane leftTableScroll = createDatasetTable(dataset, dataset.getLeft());
		JScrollPane rightTableScroll = createDatasetTable(dataset, dataset.getRight());
		rightTableScroll.setVerticalScrollBar(leftTableScroll.getVerticalScrollBar());

		GridLayout layout = new GridLayout(1, 2);
		layout.setHgap(6);

		JPanel diffTables = new JPanel();
		diffTables.setLayout(layout);
		diffTables.add(leftTableScroll);
		diffTables.add(rightTableScroll);

		datasetTabs.addTab(dataset.getName(), diffTables);
	}

	private JScrollPane createDatasetTable(final ComparedDataset dataset, final List<List<DataCell>> dataTable) {
		List<String> columnNames = dataset.getColumnNames();
		DataTableModel tableModel = new DataTableModel(columnNames, dataTable);
		JTable table = new AdjustColumnTable(tableModel);
		table.setDefaultRenderer(DataCell.class, new DataCellRenderer());
		table.setDefaultEditor(DataCell.class, new DataCellEditor(this, dataset.getLeft(), dataset.getRight()));

		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		return tableScroll;
	}

}
