package ru.snake.jdbc.diff.config;

import java.util.Collections;
import java.util.Map;

/**
 * Configuration setting. Contains join method and table configuration.
 *
 * @author snake
 *
 */
public final class Configuration {

	private static final int DEFAULT_SIMILARITY = 80;

	private FontConfig font;

	private int rowSimilarity;

	private DiffAlgorithm diffAlgorithm;

	private boolean showStatistics;

	private Map<String, BlobParserConfig> blobParsers;

	private Map<String, DriverConfig> drivers;

	/**
	 * Create empty configuration instance.
	 */
	public Configuration() {
		this.font = new FontConfig();
		this.rowSimilarity = DEFAULT_SIMILARITY;
		this.diffAlgorithm = DiffAlgorithm.GREEDY;
		this.showStatistics = false;
		this.blobParsers = Collections.emptyMap();
		this.drivers = Collections.emptyMap();
	}

	/**
	 * Returns font settings.
	 *
	 * @return font settings
	 */
	public FontConfig getFont() {
		return font;
	}

	/**
	 * Returns configured row similarity percent.
	 *
	 * @return the similarity
	 */
	public int getRowSimilarity() {
		return rowSimilarity;
	}

	/**
	 * @return the diffAlgorithm
	 */
	public DiffAlgorithm getDiffAlgorithm() {
		return diffAlgorithm;
	}

	/**
	 * @return the showStatistics
	 */
	public boolean isShowStatistics() {
		return showStatistics;
	}

	/**
	 * @return the blobMappers
	 */
	public Map<String, BlobParserConfig> getBlobParsers() {
		return blobParsers;
	}

	/**
	 * Returns map with driver configurations.
	 *
	 * @return driver map
	 */
	public Map<String, DriverConfig> getDrivers() {
		return drivers;
	}

	@Override
	public String toString() {
		return "Configuration [font=" + font + ", rowSimilarity=" + rowSimilarity + ", diffAlgorithm=" + diffAlgorithm
				+ ", showStatistics=" + showStatistics + ", blobParsers=" + blobParsers + ", drivers=" + drivers + "]";
	}

}
