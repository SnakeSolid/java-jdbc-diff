package ru.snake.jdbc.diff.listener;

import java.util.Collections;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import ru.snake.jdbc.diff.config.Configuration;
import ru.snake.jdbc.diff.config.DriverConfig;
import ru.snake.jdbc.diff.model.ConnectionParametersTableModel;
import ru.snake.jdbc.diff.model.DriverListModel;

/**
 * Listen for changing driver parameters in table and provide corresponding
 * update to connection model.
 *
 * @author snake
 *
 */
public final class ChangeParametersTableListener implements ListDataListener {

	private final Configuration config;

	private final DriverListModel driverListModel;

	private final ConnectionParametersTableModel parametersModel;

	/**
	 * Creates new update parameters model listener.
	 *
	 * @param config
	 *            configuration
	 * @param driverListModel
	 *            driver list model
	 * @param parametersModel
	 *            parameters model
	 */
	public ChangeParametersTableListener(
		final Configuration config,
		final DriverListModel driverListModel,
		final ConnectionParametersTableModel parametersModel
	) {
		this.config = config;
		this.driverListModel = driverListModel;
		this.parametersModel = parametersModel;

		updateParameters();
	}

	@Override
	public void intervalAdded(final ListDataEvent e) {
	}

	@Override
	public void intervalRemoved(final ListDataEvent e) {
	}

	@Override
	public void contentsChanged(final ListDataEvent e) {
		if (e.getType() == ListDataEvent.CONTENTS_CHANGED) {
			updateParameters();
		}
	}

	/**
	 * Update parameters using selected connection configuration.
	 */
	private void updateParameters() {
		String selectedDriver = String.valueOf(driverListModel.getSelectedItem());
		DriverConfig driverConfig = this.config.getDrivers().get(selectedDriver);

		if (driverConfig != null) {
			this.parametersModel.setParameters(selectedDriver, driverConfig.getParameters());
		} else {
			this.parametersModel.setParameters(selectedDriver, Collections.emptyList());
		}
	}

}
