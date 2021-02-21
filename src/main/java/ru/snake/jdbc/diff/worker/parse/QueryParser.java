package ru.snake.jdbc.diff.worker.parse;

import java.util.ArrayList;
import java.util.List;

import ru.snake.jdbc.diff.worker.query.Query;

/**
 * Split given text to queries.
 *
 * @author snake
 *
 */
public final class QueryParser {

	private final List<Token> tokens;

	private final List<Query> queries;

	private final StringBuilder queryText;

	private String firstComment;

	private int position;

	/**
	 * Create new parser instance.
	 *
	 * @param tokens
	 *            tokens
	 */
	private QueryParser(final List<Token> tokens) {
		this.tokens = tokens;

		this.queries = new ArrayList<>();
		this.queryText = new StringBuilder();
		this.firstComment = null;
		this.position = 0;
	}

	/**
	 * Returns query list build from parsed tokens.
	 *
	 * @return query list
	 */
	public List<Query> parse() {
		while (!isEos()) {
			Token token = tokens.get(position);

			switch (token.getType()) {
			case CODE:
				visitQueryText(token);
				break;

			case COMMENT:
				visitComment(token);
				break;

			case SEMICOLON:
				pushQuery();
				break;

			default:
				throw new IllegalStateException("Unexpected token: " + token);
			}

			position += 1;
		}

		pushQuery();

		return this.queries;
	}

	/**
	 * Push query to parsed queries. If query text is empty do nothing. Internal
	 * query variables will be cleared.
	 */
	private void pushQuery() {
		String queryName = null;
		String queryString = queryText.toString().trim();

		if (!queryString.isEmpty()) {
			if (firstComment != null) {
				queryName = firstComment.substring(2).trim();
			}

			Query query = new Query(queryName, queryString);

			queries.add(query);
		}

		queryText.setLength(0);
		firstComment = null;
	}

	/**
	 * Set current first comment to given token. If comment already exists do
	 * nothing.
	 *
	 * @param token
	 *            token
	 */
	private void visitComment(final Token token) {
		if (firstComment == null) {
			firstComment = token.getValue();
		}
	}

	/**
	 * Extend current query text with given token.
	 *
	 * @param token
	 *            token
	 */
	private void visitQueryText(final Token token) {
		queryText.append(token.getValue());
	}

	/**
	 * Returns true if end of stream reached, otherwise false.
	 *
	 * @return true if end of stream reached
	 */
	private boolean isEos() {
		return position >= tokens.size();
	}

	@Override
	public String toString() {
		return "QueryParser [tokens=" + tokens + ", queries=" + queries + ", queryText=" + queryText + ", firstComment="
				+ firstComment + ", position=" + position + "]";
	}

	/**
	 * Tokenize test to token list.
	 *
	 * @param text
	 *            text
	 * @return token list
	 */
	public static List<Query> parse(final String text) {
		List<Token> tokens = QueryTokenizer.tokenize(text);

		return new QueryParser(tokens).parse();
	}

}
