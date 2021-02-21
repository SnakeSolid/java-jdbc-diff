package ru.snake.jdbc.diff.worker;

public class TableCell {

	private final String text;

	private final Object object;

	private TableCell(final String text, final Object object) {
		this.text = text;
		this.object = object;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	@Override
	public String toString() {
		return "TableCell [text=" + text + ", object=" + object + "]";
	}

	public static TableCell empty() {
		return new TableCell(null, null);
	}

	public static TableCell text(final String text) {
		return new TableCell(text, null);
	}

	public static TableCell binary(final byte[] binary, final Object object) {
		return new TableCell(hexEncode(binary), object);
	}

	/**
	 * Returns HEX encoded string containing given byte array to string.
	 *
	 * @param value
	 *            bytes to encode
	 * @return HEX string
	 */
	private static String hexEncode(final byte[] value) {
		StringBuilder builder = new StringBuilder(2 * value.length);

		for (byte b : value) {
			builder.append(Character.forDigit((b >> 4) & 0x0f, 16));
			builder.append(Character.forDigit((b >> 0) & 0x0f, 16));
		}

		return builder.toString();
	}

}
