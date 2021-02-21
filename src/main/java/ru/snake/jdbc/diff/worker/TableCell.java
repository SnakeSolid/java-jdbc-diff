package ru.snake.jdbc.diff.worker;

import java.util.Arrays;

public class TableCell {

	private final String text;

	private final byte[] binary;

	private TableCell(final String text, final byte[] binary) {
		this.text = text;
		this.binary = binary;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the binary
	 */
	public byte[] getBinary() {
		return binary;
	}

	@Override
	public String toString() {
		return "TableCell [text=" + text + ", binary=" + Arrays.toString(binary) + "]";
	}

	public static TableCell empty() {
		return new TableCell(null, null);
	}

	public static TableCell text(final String text) {
		return new TableCell(text, null);
	}

	public static TableCell binary(final byte[] binary) {
		return new TableCell(hexEncode(binary), binary);
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
