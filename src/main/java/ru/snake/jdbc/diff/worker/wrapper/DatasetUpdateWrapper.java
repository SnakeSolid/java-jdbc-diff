package ru.snake.jdbc.diff.worker.wrapper;

import javax.swing.SwingUtilities;

import ru.snake.jdbc.diff.model.ComparedDataset;
import ru.snake.jdbc.diff.model.MainModel;

public class DatasetUpdateWrapper {

	private final MainModel model;

	private boolean changed;

	public DatasetUpdateWrapper(final MainModel model) {
		this.model = model;
		this.changed = false;
	}

	public void clearComparedDatasets() {
		SwingUtilities.invokeLater(() -> {
			model.cleanComparedDatasets();
		});
	}

	public void pushComparedDataset(final ComparedDataset datasets) {
		if (changed) {
			SwingUtilities.invokeLater(() -> {
				model.pushComparedDataset(datasets);
			});
		} else {
			changed = true;

			SwingUtilities.invokeLater(() -> {
				model.cleanComparedDatasets();
				model.pushComparedDataset(datasets);
			});
		}
	}

	@Override
	public String toString() {
		return "DatasetUpdateWrapper [model=" + model + ", changed=" + changed + "]";
	}

}
