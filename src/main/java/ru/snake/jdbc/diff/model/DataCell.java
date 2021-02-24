package ru.snake.jdbc.diff.model;

public final class DataCell {

	private final CellState state;

	private final String value;

	private final Object object;

	/**
	 * Creates new unmodifable {@link DataCell}.
	 *
	 * @param state
	 *            cell state
	 * @param value
	 *            text value
	 * @param object
	 *            object value
	 */
	private DataCell(final CellState state, final String value, final Object object) {
		this.state = state;
		this.value = value;
		this.object = object;
	}

	/**
	 * @return the state
	 */
	public CellState getState() {
		return state;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the binary
	 */
	public Object getObject() {
		return object;
	}

	@Override
	public String toString() {
		return "DataCell [state=" + state + ", value=" + value + ", object=" + object + "]";
	}

	public static DataCell valid(final String value, final Object object) {
		return new DataCell(CellState.VALID, value, object);
	}

	public static DataCell changed(final String value, final Object object) {
		return new DataCell(CellState.CHANGED, value, object);
	}

	public static DataCell missing() {
		return new DataCell(CellState.MISSING, "", null);
	}

}
