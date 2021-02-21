package ru.snake.jdbc.diff.blob;

public class DefaultBlobParserFactory implements BlobParserFactory {

	@Override
	public BlobParser getParser(String tableName, String columnName, String columnType) {
		return new DefaultBlobParser();
	}

	@Override
	public String toString() {
		return "DefaultBlobParserFactory []";
	}

}
