package ru.snake.jdbc.diff.blob;

/**
 * Creates {@link BlobParser} instances using provided table, column and data
 * types.
 *
 * @author snake
 *
 */
public interface BlobParserFactory {

	/**
	 * Returns BLOB parser to parse values from given table.
	 *
	 * @param tableName
	 *            table name
	 * @param columnName
	 *            column name
	 * @param columnType
	 *            column type
	 * @return BLOB parser
	 */
	public BlobParser getParser(String tableName, String columnName, String columnType);

}
