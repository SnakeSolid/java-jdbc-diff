package ru.snake.jdbc.diff.blob;

/**
 * Creates default instance of simple BLOB parser {@link DefaultBlobParser} for
 * any table, column and type.
 *
 * @author snake
 *
 */
public final class DefaultBlobParserFactory implements BlobParserFactory {

	@Override
	public BlobParser getParser(final String tableName, final String columnName, final String columnType) {
		return new DefaultBlobParser();
	}

	@Override
	public String toString() {
		return "DefaultBlobParserFactory []";
	}

}
