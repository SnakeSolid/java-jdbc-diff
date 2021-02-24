package ru.snake.jdbc.diff.blob;

/**
 * BLOB field parser. Parsers binary database field to JSON-like object
 * structure.
 *
 * @author snake
 *
 */
public interface BlobParser {

	/**
	 * Parse BLOB and create object with known structure consisting of probably
	 * nested Maps, Lists, Strings and nulls.
	 *
	 * @param blob
	 *            BLOB to parse
	 * @return parsed object
	 */
	Object parse(byte[] blob);

}
