package ru.snake.jdbc.diff.config;

/**
 * BLOB parser configuration settings. Contains parser library path and class
 * name.
 *
 * @author snake
 *
 */
public final class BlobParserConfig {

	private String libraryPath;

	private String parserClass;

	/**
	 * Create empty parser settings.
	 */
	public BlobParserConfig() {
		this.libraryPath = null;
		this.parserClass = null;
	}

	/**
	 * @return the libraryPath
	 */
	public String getLibraryPath() {
		return libraryPath;
	}

	/**
	 * @return the parserClass
	 */
	public String getParserClass() {
		return parserClass;
	}

	@Override
	public String toString() {
		return "BlobParserConfig [libraryPath=" + libraryPath + ", parserClass=" + parserClass + "]";
	}

}
