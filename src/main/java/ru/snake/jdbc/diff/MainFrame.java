package ru.snake.jdbc.diff;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
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
import ru.snake.jdbc.diff.action.ExecuteSelectedAction;
import ru.snake.jdbc.diff.action.FirstDifferenceAction;
import ru.snake.jdbc.diff.action.LastDifferenceAction;
import ru.snake.jdbc.diff.action.NewFileAction;
import ru.snake.jdbc.diff.action.NextDifferenceAction;
import ru.snake.jdbc.diff.action.OpenFileAction;
import ru.snake.jdbc.diff.action.PrevDifferenceAction;
import ru.snake.jdbc.diff.action.RedoAction;
import ru.snake.jdbc.diff.action.SaveAsFileAction;
import ru.snake.jdbc.diff.action.SaveFileAction;
import ru.snake.jdbc.diff.action.SelectConnectionAction;
import ru.snake.jdbc.diff.action.SettingsAction;
import ru.snake.jdbc.diff.action.StatisticsAction;
import ru.snake.jdbc.diff.action.TextEditActionAdapter;
import ru.snake.jdbc.diff.action.UndoAction;
import ru.snake.jdbc.diff.component.DiffPanel;
import ru.snake.jdbc.diff.config.Configuration;
import ru.snake.jdbc.diff.dialog.ConnectionDialog;
import ru.snake.jdbc.diff.dialog.ObjectCompareDialog;
import ru.snake.jdbc.diff.dialog.ObjectViewDialog;
import ru.snake.jdbc.diff.dialog.SettingsDialog;
import ru.snake.jdbc.diff.dialog.StatisticsDialog;
import ru.snake.jdbc.diff.listener.TextEditorMouseListener;
import ru.snake.jdbc.diff.model.CompareSettings;
import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.MainModel;
import ru.snake.jdbc.diff.model.listener.ComparedDatasetListener;
import ru.snake.jdbc.diff.model.listener.ExecutingStateListener;

/**
 * Main application frame.
 *
 * @author snake
 *
 */
public final class MainFrame extends JFrame implements ComparedDatasetListener, ExecutingStateListener {

	private static final int PREFERRED_WIDTH = 800;

	private static final int PREFERRED_HEIGHT = 600;

	private static final float CARET_ASPECT_RATIO = 0.1f;

	private static final int DEFAULT_DIVIDER_LOCATION = 350;

	private final Configuration config;

	private final MainModel model;

	private JFileChooser queryChooser;

	private SettingsDialog settingsDialog;

	private ConnectionDialog connectionDialog;

	private ObjectCompareDialog objectCompareDialog;

	private ObjectViewDialog objectViewDialog;

	private StatisticsDialog statisticsDialog;

	private Action newFileAction;

	private Action openFileAction;

	private Action saveFileAction;

	private Action saveAsFileAction;

	private SettingsAction settingsAction;

	private CloseFrameAction closeFrameAction;

	private SelectConnectionAction prepareConnectionAction;

	private ExecuteQueryAction executeQueryAction;

	private ExecuteSelectedAction executeSelectedAction;

	private StatisticsAction statisticsAction;

	private FirstDifferenceAction firstDifferenceAction;

	private NextDifferenceAction nextDifferenceAction;

	private PrevDifferenceAction prevDifferenceAction;

	private LastDifferenceAction lastDifferenceAction;

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
		model.addExecutingListener(this);
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
		settingsAction = new SettingsAction(this);
		closeFrameAction = new CloseFrameAction(this);
		prepareConnectionAction = new SelectConnectionAction(this);
		executeQueryAction = new ExecuteQueryAction(this, config);
		executeSelectedAction = new ExecuteSelectedAction(this, config);
		statisticsAction = new StatisticsAction(this);
		firstDifferenceAction = new FirstDifferenceAction(this);
		nextDifferenceAction = new NextDifferenceAction(this);
		prevDifferenceAction = new PrevDifferenceAction(this);
		lastDifferenceAction = new LastDifferenceAction(this);
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
		fileMenu.add(settingsAction);
		fileMenu.addSeparator();
		fileMenu.add(closeFrameAction);

		JMenu connectionMenu = new JMenu("Connection");
		connectionMenu.setMnemonic('C');
		connectionMenu.add(prepareConnectionAction);
		connectionMenu.add(executeQueryAction);
		connectionMenu.add(executeSelectedAction);

		JMenu differenceMenu = new JMenu("Difference");
		differenceMenu.setMnemonic('D');
		differenceMenu.add(statisticsAction);
		differenceMenu.addSeparator();
		differenceMenu.add(firstDifferenceAction);
		differenceMenu.add(prevDifferenceAction);
		differenceMenu.add(nextDifferenceAction);
		differenceMenu.add(lastDifferenceAction);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(connectionMenu);
		menuBar.add(differenceMenu);

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
		toolBar.add(executeSelectedAction);
		toolBar.addSeparator();
		toolBar.add(statisticsAction);
		toolBar.addSeparator();
		toolBar.add(firstDifferenceAction);
		toolBar.add(prevDifferenceAction);
		toolBar.add(nextDifferenceAction);
		toolBar.add(lastDifferenceAction);

		add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Create all required components on frame.
	 */
	private void createComponents() {
		Font configuredFont = getConfigFont();
		queryText = new JTextPane(this.model.getQueryDocument());
		queryText.addCaretListener(executeSelectedAction);
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
		datasetTabs.addChangeListener(firstDifferenceAction);
		datasetTabs.addChangeListener(prevDifferenceAction);
		datasetTabs.addChangeListener(nextDifferenceAction);
		datasetTabs.addChangeListener(lastDifferenceAction);

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
		Document document = textComponent.getDocument();
		Action undoAction = new UndoAction(document, undoManager);
		Action redoAction = new RedoAction(document, undoManager);
		document.addUndoableEditListener(undoManager);

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

		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(new TextEditActionAdapter(cut, "Cut"));
		popupMenu.add(new TextEditActionAdapter(copy, "Copy"));
		popupMenu.add(new TextEditActionAdapter(paste, "Paste"));
		popupMenu.addSeparator();
		popupMenu.add(new TextEditActionAdapter(selectAll, "Select all"));

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
	 * Returns lay initialized {@link SettingsDialog} to set preferences.
	 *
	 * @return settings dialog
	 */
	public SettingsDialog getSettingsDialog() {
		if (settingsDialog == null) {
			settingsDialog = new SettingsDialog(this, config);
		}

		return settingsDialog;
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
	 * Returns lay initialized {@link ObjectViewDialog} to select connection
	 * settings.
	 *
	 * @return object view dialog
	 */
	public ObjectViewDialog getObjectViewDialog() {
		if (objectViewDialog == null) {
			objectViewDialog = new ObjectViewDialog(this);
		}

		return objectViewDialog;
	}

	/**
	 * Returns lay initialized {@link StatisticsDialog} to show compare
	 * statistics.
	 *
	 * @return statistics dialog
	 */
	public StatisticsDialog getStatisticsDialog() {
		if (statisticsDialog == null) {
			statisticsDialog = new StatisticsDialog(this);
		}

		return statisticsDialog;
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
	public void comparedDatasetsCleared(final MainModel aModel) {
		if (model == aModel) {
			datasetTabs.removeAll();
		}
	}

	@Override
	public void comparedDatasetPushed(final MainModel aModel, final ComparedDataset dataset) {
		if (model == aModel) {
			DiffPanel diffPanel = new DiffPanel(this, dataset);

			datasetTabs.addTab(dataset.getName(), diffPanel);
		}
	}

	@Override
	public void executingStateChanged(final MainModel aModel, final boolean executing, final boolean success) {
		if (model == aModel && !executing && success) {
			boolean showStatistics = Optional.ofNullable(model.getCompareSettings())
				.map(CompareSettings::isShowStatistics)
				.orElse(false);

			if (showStatistics) {
				StatisticsDialog dialog = getStatisticsDialog();
				dialog.setStatistics(model.getComparedDatasets());
				dialog.setVisible(true);
			}
		}
	}

}
