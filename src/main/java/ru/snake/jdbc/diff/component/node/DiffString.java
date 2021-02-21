package ru.snake.jdbc.diff.component.node;

public class DiffString {

	private final DiffState state;

	private final String value;

	private final Object object;

	public DiffString(final DiffState state, final String value, final Object object) {
		this.state = state;
		this.value = value;
		this.object = object;
	}

	/**
	 * @return the state
	 */
	public DiffState getState() {
		return state;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	@Override
	public String toString() {
		return "DiffString [state=" + state + ", value=" + value + ", object=" + object + "]";
	}

}
