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

	private int rowSimilarity;

	private FontConfig font;

	private Map<String, BlobParserConfig> blobParsers;

	private Map<String, DriverConfig> drivers;

	/**
	 * Create empty configuration instance.
	 */
	public Configuration() {
		this.rowSimilarity = DEFAULT_SIMILARITY;
		this.font = new FontConfig();
		this.blobParsers = Collections.emptyMap();
		this.drivers = Collections.emptyMap();
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
	 * @param similarity
	 *            the similarity to set
	 */
	public void setSimilarity(final int similarity) {
		this.rowSimilarity = similarity;
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
		return "Configuration [similarity=" + rowSimilarity + ", font=" + font + ", blobParsers=" + blobParsers
				+ ", drivers=" + drivers + "]";
	}

}
