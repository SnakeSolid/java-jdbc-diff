package ru.snake.jdbc.diff.model;

import java.util.Set;

/**
 * Connection settings from worker. Settings contain field mappings and driver
 * settings.
 *
 * @author snake
 *
 */
public final class ConnectionSettings {

	private final String driverPath;

	private final String driverClass;

	private final Set<String> binaryTypes;

	private final String parserLibrary;

	private final String parserClass;

	private final String url;

	/**
	 * Creates new connection settings using given driver path and URL.
	 *
	 * @param driverPath
	 *            driver path
	 * @param driverClass
	 *            driver class name
	 * @param binaryTypes
	 *            binary type names
	 * @param url
	 *            connection URL
	 * @param url
	 * @param parserClass
	 */
	public ConnectionSettings(
		final String driverPath,
		final String driverClass,
		final Set<String> binaryTypes,
		final String parserLibrary,
		final String parserClass,
		final String url
	) {
		this.driverPath = driverPath;
		this.driverClass = driverClass;
		this.binaryTypes = binaryTypes;
		this.parserLibrary = parserLibrary;
		this.parserClass = parserClass;
		this.url = url;
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
	 * Returns binary type names in this connections.
	 *
	 * @return binary type names
	 */
	public Set<String> getBinaryTypes() {
		return binaryTypes;
	}

	/**
	 * Returns parser library to use for BLOB fields in this connection. If no
	 * library provided - return {@code null}.
	 *
	 * @return parser library path
	 */
	public String getParserLibrary() {
		return parserLibrary;
	}

	/**
	 * Returns parsers class to use for BLOB fields in this connection. If no
	 * class provided - return {@code null}.
	 *
	 * @return parser class name
	 */
	public String getParserClass() {
		return parserClass;
	}

	/**
	 * Returns JDBC connection URL.
	 *
	 * @return connection URL
	 */
	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "ConnectionSettings [driverPath=" + driverPath + ", driverClass=" + driverClass + ", binaryTypes="
				+ binaryTypes + ", parserLibrary=" + parserLibrary + ", parserClass=" + parserClass + ", url=" + url
				+ "]";
	}

}
