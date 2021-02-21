package ru.snake.jdbc.diff.worker.query;

/**
 * Data transfer object for single query and table name.
 *
 * @author snake
 *
 */
public final class Query {

	private final String queryName;

	private final String queryText;

	/**
	 * Create query from table name and query test.
	 *
	 * @param queryName
	 *            query name
	 * @param queryText
	 *            query text
	 */
	public Query(final String queryName, final String queryText) {
		this.queryName = queryName;
		this.queryText = queryText;
	}

	/**
	 * Returns query name or null.
	 *
	 * @return query name
	 */
	public String getQueryName() {
		return queryName;
	}

	/**
	 * Returns query text.
	 *
	 * @return query text
	 */
	public String getQueryText() {
		return queryText;
	}

	@Override
	public String toString() {
		return "Query [queryName=" + queryName + ", queryText=" + queryText + "]";
	}

}
