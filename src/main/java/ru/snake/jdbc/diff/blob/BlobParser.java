package ru.snake.jdbc.diff.blob;

public interface BlobParser {

	/**
	 * Parse BLOB and create object with known structure consisting of probably
	 * nested Maps, Lists, Strings and nulls.
	 *
	 * @param blob
	 *            BLOB to parse
	 * @return parsed object
	 */
	public Object parse(final byte[] blob);

}
