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

	private static final Set<String> SQL_KEYWORDS;

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
		TABLE_NAMES.add(Pattern.compile("\\bfrom\\s+(\\w+(\\.\\w+)", Pattern.CASE_INSENSITIVE));
		TABLE_NAMES.add(Pattern.compile("\\bjoin\\s+(\\w+(\\.\\w+)", Pattern.CASE_INSENSITIVE));

		// SQL keywords not allowed as table alias
		SQL_KEYWORDS = new HashSet<>();
		SQL_KEYWORDS.add("inner");
		SQL_KEYWORDS.add("left");
		SQL_KEYWORDS.add("right");
		SQL_KEYWORDS.add("cross");
		SQL_KEYWORDS.add("natural");
		SQL_KEYWORDS.add("join");
		SQL_KEYWORDS.add("using");
		SQL_KEYWORDS.add("on");
		SQL_KEYWORDS.add("where");
		SQL_KEYWORDS.add("having");
		SQL_KEYWORDS.add("group");
		SQL_KEYWORDS.add("order");
		SQL_KEYWORDS.add("limit");
		SQL_KEYWORDS.add("into");
		SQL_KEYWORDS.add("for");
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
				String tableAlias = matcher.group(5);

				if (tableAlias == null || SQL_KEYWORDS.contains(tableAlias.toLowerCase())) {
					result.add(tableName);
				} else {
					result.add(tableName + " (" + tableAlias + ")");
				}
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
