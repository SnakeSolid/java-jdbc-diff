package ru.snake.jdbc.diff.config;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * JDBC driver configuration settings.
 *
 * @author snake
 *
 */
public final class DriverConfig {

	private String driverPath;

	private String driverClass;

	private String url;

	private Set<String> binaryTypes;

	private List<String> parameters;

	/**
	 * Create empty driver settings.
	 */
	public DriverConfig() {
		this.driverPath = null;
		this.driverClass = null;
		this.url = null;
		this.binaryTypes = Collections.emptySet();
		this.parameters = Collections.emptyList();
	}

	/**
	 * Return path to JDBC driver library.
	 *
	 * @return path to driver
	 */
	public String getDriverPath() {
		return driverPath;
	}

	/**
	 * Returns JDBC driver class name.
	 *
	 * @return driver class name
	 */
	public String getDriverClass() {
		return driverClass;
	}

	/**
	 * Returns JDBC connection URL.
	 *
	 * @return connection URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Returns list of driver binary types.
	 *
	 * @return list of binary types for this driver
	 */
	public Set<String> getBinaryTypes() {
		return binaryTypes;
	}

	/**
	 * Returns list of available connection parameters.
	 *
	 * @return list of connection parameter
	 */
	public List<String> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return "DriverConfig [driverPath=" + driverPath + ", driverClass=" + driverClass + ", url=" + url
				+ ", binaryTypes=" + binaryTypes + ", parameters=" + parameters + "]";
	}

}
