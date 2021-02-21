package ru.snake.jdbc.diff.algorithm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

public class DiffObjectTest {

	@Test
	public void shouldBuildDiffWhenSequenceEmpty() {
		Object left = null;
		Object right = null;
		DiffObject diffObject = new DiffObject(left, right, Objects::equals);
		@SuppressWarnings("unchecked")
		DiffObjectItem<String> result = (DiffObjectItem<String>) diffObject.diff();

		assertThat(result.getType(), is(DiffType.BOTH));
		assertThat(result.getLeft(), nullValue());
		assertThat(result.getRight(), nullValue());
	}

	@Test
	public void shouldBuildDiffWhenSequenceLeftFilled() {
		Object left = "a";
		Object right = null;
		DiffObject diffObject = new DiffObject(left, right, Objects::equals);
		@SuppressWarnings("unchecked")
		DiffObjectItem<String> result = (DiffObjectItem<String>) diffObject.diff();

		assertThat(result.getType(), is(DiffType.LEFT));
		assertThat(result.getLeft(), is("a"));
		assertThat(result.getRight(), nullValue());
	}

	@Test
	public void shouldBuildDiffWhenSequenceRightFilled() {
		Object left = null;
		Object right = "a";
		DiffObject diffObject = new DiffObject(left, right, Objects::equals);
		@SuppressWarnings("unchecked")
		DiffObjectItem<String> result = (DiffObjectItem<String>) diffObject.diff();

		assertThat(result.getType(), is(DiffType.RIGHT));
		assertThat(result.getLeft(), nullValue());
		assertThat(result.getRight(), is("a"));
	}

	@Test
	public void shouldBuildDiffWhenDifferentStrings() {
		Object left = "a";
		Object right = "b";
		DiffObject diffObject = new DiffObject(left, right, Objects::equals);
		@SuppressWarnings("unchecked")
		DiffObjectItem<String> result = (DiffObjectItem<String>) diffObject.diff();

		assertThat(result.getType(), is(DiffType.UPDATE));
		assertThat(result.getLeft(), is("a"));
		assertThat(result.getRight(), is("b"));
	}

	@Test
	public void shouldBuildDiffWhenDifferentTypes() {
		Object left = Arrays.asList("a", "b");
		Object right = Collections.singletonMap("c", "d");
		DiffObject diffObject = new DiffObject(left, right, Objects::equals);
		@SuppressWarnings("unchecked")
		DiffObjectItem<Object> result = (DiffObjectItem<Object>) diffObject.diff();

		assertThat(result.getType(), is(DiffType.UPDATE));
		assertThat(result.getLeft(), is(left));
		assertThat(result.getRight(), is(right));
	}

	@Test
	public void shouldBuildDiffWhenDifferenceInList() {
		Object left = Collections.singletonMap("k", Arrays.asList("a", "b"));
		Object right = Collections.singletonMap("k", Arrays.asList("a", "c"));
		DiffObject diffObject = new DiffObject(left, right, Objects::equals);
		@SuppressWarnings("unchecked")
		Map<String, List<DiffObjectItem<String>>> result = (Map<String, List<DiffObjectItem<String>>>) diffObject
			.diff();

		assertThat(result.size(), is(1));
		assertThat(result.get("k").size(), is(3));
		assertThat(result.get("k").get(0).getType(), is(DiffType.BOTH));
		assertThat(result.get("k").get(1).getType(), is(DiffType.LEFT));
		assertThat(result.get("k").get(2).getType(), is(DiffType.RIGHT));
		assertThat(result.get("k").get(0).getLeft(), is("a"));
		assertThat(result.get("k").get(1).getLeft(), is("b"));
		assertThat(result.get("k").get(2).getLeft(), nullValue());
		assertThat(result.get("k").get(0).getRight(), is("a"));
		assertThat(result.get("k").get(1).getRight(), nullValue());
		assertThat(result.get("k").get(2).getRight(), is("c"));
	}

	@Test
	public void shouldBuildDiffWhenDifferenceInMap() {
		Object left = Arrays.asList(Collections.singletonMap("a", "b"), Collections.singletonMap("c", "d"));
		Object right = Arrays.asList(Collections.singletonMap("a", "b"), Collections.singletonMap("e", "f"));
		DiffObject diffObject = new DiffObject(left, right, Objects::equals);
		@SuppressWarnings("unchecked")
		List<DiffObjectItem<Map<String, String>>> result = (List<DiffObjectItem<Map<String, String>>>) diffObject
			.diff();

		assertThat(result.size(), is(3));
		assertThat(result.get(0).getType(), is(DiffType.BOTH));
		assertThat(result.get(0).getLeft(), is(Collections.singletonMap("a", "b")));
		assertThat(result.get(0).getRight(), is(Collections.singletonMap("a", "b")));
		assertThat(result.get(1).getType(), is(DiffType.LEFT));
		assertThat(result.get(1).getLeft(), is(Collections.singletonMap("c", "d")));
		assertThat(result.get(1).getRight(), nullValue());
		assertThat(result.get(2).getType(), is(DiffType.RIGHT));
		assertThat(result.get(2).getLeft(), is(nullValue()));
		assertThat(result.get(2).getRight(), is(Collections.singletonMap("e", "f")));
	}

}
