package ru.snake.jdbc.diff.worker.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create sequence of queries from single query. Query will be used as template.
 *
 * @author snake
 *
 */
public final class QueryTable {

	private static final Collection<Pattern> NO_SUBSTITUTE;

	private static final Collection<Pattern> TABLE_NAMES;

	private final String queryText;

	static {
		// Tables in query can't be substituted if
		NO_SUBSTITUTE = new ArrayList<>();
		NO_SUBSTITUTE.add(Pattern.compile("\\bwith\\s+", Pattern.CASE_INSENSITIVE));
		NO_SUBSTITUTE.add(Pattern.compile("\\bfrom\\s*\\(", Pattern.CASE_INSENSITIVE));
		NO_SUBSTITUTE.add(Pattern.compile("\\bjoin\\s*\\(", Pattern.CASE_INSENSITIVE));
		NO_SUBSTITUTE.add(Pattern.compile("\\(\\s*select\\b", Pattern.CASE_INSENSITIVE));

		// Table name patterns from SQL
		TABLE_NAMES = new ArrayList<>();
		TABLE_NAMES.add(Pattern.compile("\\bfrom\\s+(\\w+(\\.\\w+)?)", Pattern.CASE_INSENSITIVE));
		TABLE_NAMES.add(Pattern.compile("\\bjoin\\s+(\\w+(\\.\\w+)?)", Pattern.CASE_INSENSITIVE));
	}

	/**
	 * Create new query template using given text.
	 *
	 * @param queryText
	 *            query text
	 */
	private QueryTable(final String queryText) {
		this.queryText = queryText;
	}

	/**
	 * Find all table names in query and returns them as set. If table was not
	 * found returns empty set.
	 *
	 * @return table name list
	 */
	private Set<String> tableName() {
		for (Pattern pattern : NO_SUBSTITUTE) {
			if (pattern.matcher(queryText).find()) {
				return Collections.emptySet();
			}
		}

		Set<String> result = new HashSet<>();

		for (Pattern pattern : TABLE_NAMES) {
			Matcher matcher = pattern.matcher(queryText);

			while (matcher.find()) {
				String tableName = matcher.group(1);

				result.add(tableName);
			}
		}

		return result;
	}

	/**
	 * Generate queries using given query template.
	 *
	 * @param query
	 *            query template
	 * @return query list or error
	 */
	public static Optional<String> tableName(final Query query) {
		Set<String> tableNames = new QueryTable(query.getQueryText()).tableName();

		if (tableNames.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(tableNames.iterator().next());
		}
	}

	@Override
	public String toString() {
		return "QueryTable [queryText=" + queryText + "]";
	}

}
