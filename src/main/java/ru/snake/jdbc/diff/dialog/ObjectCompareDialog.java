package ru.snake.jdbc.diff.dialog;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.action.CloseDialogAction;
import ru.snake.jdbc.diff.algorithm.DiffObject;
import ru.snake.jdbc.diff.algorithm.DiffObjectItem;
import ru.snake.jdbc.diff.algorithm.DiffType;
import ru.snake.jdbc.diff.component.DiffTreeTable;
import ru.snake.jdbc.diff.component.cell.DiffStringCellEditor;
import ru.snake.jdbc.diff.component.cell.DiffStringCellRenderer;
import ru.snake.jdbc.diff.component.model.DiffAbstractTreeTableModel;
import ru.snake.jdbc.diff.component.model.DiffDataModel;
import ru.snake.jdbc.diff.component.node.DiffDataNode;
import ru.snake.jdbc.diff.component.node.DiffState;
import ru.snake.jdbc.diff.component.node.DiffString;

/**
 * Compare objects dialog. Show difference between two objects.
 *
 * @author snake
 *
 */
public class ObjectCompareDialog extends JDialog {

	private static final int DEFAULT_HEIGHT = 640;

	private static final int DEFAULT_WIDTH = 640;

	private final MainFrame mainFrame;

	private DiffTreeTable diffTree;

	public ObjectCompareDialog(final MainFrame mainFrame) {
		super(mainFrame, "Object Diff", true);

		this.mainFrame = mainFrame;

		initializeComponents();

		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		pack();
		setLocationRelativeTo(mainFrame);
	}

	private void initializeComponents() {
		DiffAbstractTreeTableModel treeTableModel = new DiffDataModel(null);
		diffTree = new DiffTreeTable(treeTableModel);
		diffTree.setDefaultRenderer(DiffString.class, new DiffStringCellRenderer());
		diffTree.setDefaultEditor(DiffString.class, new DiffStringCellEditor(mainFrame));

		JScrollPane diffScroll = new JScrollPane(
			this.diffTree,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);

		CloseDialogAction closeAction = new CloseDialogAction(this);
		JButton buttonClose = new JButton(closeAction);
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = this.getRootPane();

		rootPane.registerKeyboardAction(closeAction, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

		Container contentPane = getContentPane();
		GroupLayout layout = new GroupLayout(contentPane);
		contentPane.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.TRAILING).addComponent(diffScroll).addComponent(buttonClose)
		);
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(diffScroll).addComponent(buttonClose));
	}

	/**
	 * Compare two objects and set result as dialog model.
	 *
	 * @param left
	 *            left object
	 * @param right
	 *            right object
	 */
	public void compareObjects(final Object left, final Object right) {
		Object objectDiff = new DiffObject(left, right, Object::equals).diff();
		DiffDataNode root = buildDataNode(objectDiff);
		DiffAbstractTreeTableModel model = new DiffDataModel(root);
		diffTree.setDiffTreeTableModel(model);
	}

	private static DiffDataNode buildDataNode(final Object object) {
		return buildDataNodeRecursive("", object);
	}

	private static DiffDataNode buildDataNodeRecursive(final String name, final Object object) {
		Class<?> clazz = object.getClass();

		if (DiffObjectItem.class.isAssignableFrom(clazz)) {
			DiffObjectItem<?> diffItem = (DiffObjectItem<?>) object;

			return buildNode(
				name,
				diffItem.getType(),
				diffItem.getLeft(),
				diffItem.getRight(),
				Collections.emptyList()
			);
		} else if (List.class.isAssignableFrom(clazz)) {
			@SuppressWarnings("unchecked")
			List<Object> objectList = (List<Object>) object;
			List<DiffDataNode> nodes = new ArrayList<>();
			int index = 0;

			for (Object entry : objectList) {
				String itemName = String.format("%d.", index);

				nodes.add(buildDataNodeRecursive(itemName, entry));

				index += 1;
			}

			return buildNode(name, DiffType.BOTH, "", "", nodes);
		} else if (Map.class.isAssignableFrom(clazz)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> objectMap = (Map<String, Object>) object;
			List<DiffDataNode> nodes = new ArrayList<>();

			for (Entry<String, Object> entry : objectMap.entrySet()) {
				String itemName = entry.getKey();
				Object itemObject = entry.getValue();

				nodes.add(buildDataNodeRecursive(itemName, itemObject));
			}

			return buildNode(name, DiffType.BOTH, "", "", nodes);
		} else {
			return buildNode("name", DiffType.BOTH, object, object, Collections.emptyList());
		}
	}

	/**
	 * Build difference node using given left and right values.
	 *
	 * @param name
	 *            node name
	 * @param type
	 *            difference type
	 * @param left
	 *            left value
	 * @param right
	 *            right value
	 * @param chilren
	 *            children nodes
	 * @return difference node
	 */
	private static DiffDataNode buildNode(
		final String name,
		final DiffType type,
		final Object left,
		final Object right,
		final List<DiffDataNode> chilren
	) {
		String leftString = buildString(left);
		String rightString = buildString(right);

		switch (type) {
		case BOTH:
			return new DiffDataNode(
				name,
				new DiffString(DiffState.EQUALS, leftString, left),
				new DiffString(DiffState.EQUALS, rightString, right),
				chilren
			);

		case LEFT:
			return new DiffDataNode(
				name,
				new DiffString(DiffState.EQUALS, leftString, left),
				new DiffString(DiffState.REMOVED, rightString, right),
				chilren
			);

		case RIGHT:
			return new DiffDataNode(
				name,
				new DiffString(DiffState.REMOVED, leftString, left),
				new DiffString(DiffState.EQUALS, rightString, right),
				chilren
			);

		case UPDATE:
			return new DiffDataNode(
				name,
				new DiffString(DiffState.CHANGED, leftString, left),
				new DiffString(DiffState.CHANGED, rightString, right),
				chilren
			);

		default:
			throw new RuntimeException("Invalid diff type");
		}
	}

	/**
	 * Build string value from object. If object is null returns empty string.
	 *
	 * @param value
	 *            object
	 * @return string value
	 */
	private static String buildString(final Object value) {
		if (value == null) {
			return "";
		} else {
			return String.valueOf(value);
		}
	}

}
