package ru.snake.jdbc.diff.document.parse;

/**
 * Represents SQL token with type and stream position.
 *
 * @author snake
 *
 */
public final class Token {

	private final TokenType type;

	private final int offset;

	private final int length;

	/**
	 * Creates new token with given type.
	 *
	 * @param type
	 *            token type
	 * @param offset
	 *            offset
	 * @param length
	 *            length
	 */
	private Token(final TokenType type, final int offset, final int length) {
		this.type = type;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Returns given token type.
	 *
	 * @return token type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Returns given offset.
	 *
	 * @return offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Returns given length.
	 *
	 * @return length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Create new token with type {@link TokenType#STRING} and given value.
	 *
	 * @param offset
	 *            offset
	 * @param length
	 *            length
	 * @return new token
	 */
	public static Token string(final int offset, final int length) {
		return new Token(TokenType.STRING, offset, length);
	}

	/**
	 * Create new token with type {@link TokenType#COMMENT} and given value.
	 *
	 * @param offset
	 *            offset
	 * @param length
	 *            length
	 * @return new token
	 */
	public static Token comment(final int offset, final int length) {
		return new Token(TokenType.COMMENT, offset, length);
	}

	/**
	 * Create new token with type {@link TokenType#OTHER} and given value.
	 *
	 * @param offset
	 *            offset
	 * @param length
	 *            length
	 * @return new token
	 */
	public static Token other(final int offset, final int length) {
		return new Token(TokenType.OTHER, offset, length);
	}

	/**
	 * Create new token with type {@link TokenType#KEYWORD} and given value.
	 *
	 * @param offset
	 *            offset
	 * @param length
	 *            length
	 * @return new token
	 */
	public static Token keyword(final int offset, final int length) {
		return new Token(TokenType.KEYWORD, offset, length);
	}

	@Override
	public String toString() {
		return "Token [type=" + type + ", offset=" + offset + ", length=" + length + "]";
	}

}
