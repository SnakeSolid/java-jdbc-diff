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
import ru.snake.jdbc.diff.component.DiffTreeTable;
import ru.snake.jdbc.diff.component.model.DiffAbstractTreeTableModel;
import ru.snake.jdbc.diff.component.model.DiffDataModel;
import ru.snake.jdbc.diff.component.model.ObjectViewModel;
import ru.snake.jdbc.diff.component.node.ObjectNode;

/**
 * Show object dialog. Show given object as tree table.
 *
 * @author snake
 *
 */
public class ObjectViewDialog extends JDialog {

	private static final int DEFAULT_HEIGHT = 480;

	private static final int DEFAULT_WIDTH = 480;

	private DiffTreeTable diffTree;

	public ObjectViewDialog(final MainFrame mainFrame) {
		super(mainFrame, "Object View", true);

		DiffAbstractTreeTableModel treeTableModel = new ObjectViewModel(null);
		this.diffTree = new DiffTreeTable(treeTableModel);

		initializeComponents();

		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		pack();
		setLocationRelativeTo(mainFrame);
	}

	private void initializeComponents() {
		DiffAbstractTreeTableModel treeTableModel = new DiffDataModel(null);
		diffTree = new DiffTreeTable(treeTableModel);

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
	 * Convert given object to tree model and set model as tree table model.
	 *
	 * @param object
	 *            object
	 */
	public void setViewObject(final Object object) {
		ObjectNode root = buildObjectNode(object);
		DiffAbstractTreeTableModel model = new ObjectViewModel(root);
		diffTree.setDiffTreeTableModel(model);
	}

	/**
	 * Convert given object to {@link ObjectNode}.
	 *
	 * @param object
	 *            object
	 * @return object node
	 */
	private static ObjectNode buildObjectNode(final Object object) {
		return buildObjectNodeRecursive("", object);
	}

	/**
	 * Recursively convert given object to {@link ObjectNode}.
	 *
	 * @param name
	 *            node name
	 * @param object
	 *            object
	 * @return object node
	 */
	private static ObjectNode buildObjectNodeRecursive(final String name, final Object object) {
		if (object == null) {
			return new ObjectNode(name, null, Collections.emptyList());
		}

		Class<?> clazz = object.getClass();

		if (List.class.isAssignableFrom(clazz)) {
			@SuppressWarnings("unchecked")
			List<Object> objectList = (List<Object>) object;
			List<ObjectNode> nodes = new ArrayList<>();
			int index = 0;

			for (Object entry : objectList) {
				String itemName = String.format("%d.", index);

				nodes.add(buildObjectNodeRecursive(itemName, entry));

				index += 1;
			}

			return new ObjectNode(name, "", nodes);
		} else if (Map.class.isAssignableFrom(clazz)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> objectMap = (Map<String, Object>) object;
			List<ObjectNode> nodes = new ArrayList<>();

			for (Entry<String, Object> entry : objectMap.entrySet()) {
				String itemName = entry.getKey();
				Object itemObject = entry.getValue();

				nodes.add(buildObjectNodeRecursive(itemName, itemObject));
			}

			return new ObjectNode(name, "", nodes);
		} else {
			return new ObjectNode(name, String.valueOf(object), Collections.emptyList());
		}
	}

}
