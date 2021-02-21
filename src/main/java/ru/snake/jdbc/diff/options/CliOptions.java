package ru.snake.jdbc.diff.options;

import java.io.File;

/**
 * Structure contains all options.
 *
 * @author snake
 *
 */
public class CliOptions {

	private final File configFile;

	/**
	 * Create new CLI options using given configuration file path.
	 *
	 * @param configFile
	 *            configuration file path
	 */
	public CliOptions(final File configFile) {
		this.configFile = configFile;
	}

	/**
	 * Returns configFile option value.
	 *
	 * @return configFile value
	 */
	public File getConfigFile() {
		return configFile;
	}

	@Override
	public String toString() {
		return "CliOptions [configFile=" + configFile + "]";
	}

}
