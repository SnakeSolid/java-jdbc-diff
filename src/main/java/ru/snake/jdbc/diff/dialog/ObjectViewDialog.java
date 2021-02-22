package ru.snake.jdbc.diff.dialog;

import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import ru.snake.jdbc.diff.MainFrame;
import ru.snake.jdbc.diff.action.CloseDialogAction;
import ru.snake.jdbc.diff.component.DiffTreeTable;
import ru.snake.jdbc.diff.component.model.DiffAbstractTreeTableModel;
import ru.snake.jdbc.diff.component.model.DiffDataModel;
import ru.snake.jdbc.diff.component.model.ObjectViewModel;
import ru.snake.jdbc.diff.component.node.ObjectNode;

public class ObjectViewDialog extends JDialog {

	private DiffTreeTable diffTree;

	public ObjectViewDialog(final MainFrame mainFrame) {
		super(mainFrame, "Object View", true);

		DiffAbstractTreeTableModel treeTableModel = new ObjectViewModel(null);
		this.diffTree = new DiffTreeTable(treeTableModel);

		initializeComponents();

		setPreferredSize(new Dimension(480, 480));
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

	public void setViewObject(Object object) {
		ObjectNode root = buildObjectNode(object);
		DiffAbstractTreeTableModel model = new ObjectViewModel(root);
		diffTree.setDiffTreeTableModel(model);
	}

	private static ObjectNode buildObjectNode(Object object) {
		return buildObjectNodeRecursive("", object);
	}

	private static ObjectNode buildObjectNodeRecursive(String name, Object object) {
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
