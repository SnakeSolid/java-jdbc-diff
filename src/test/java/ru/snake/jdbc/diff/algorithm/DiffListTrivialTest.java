package ru.snake.jdbc.diff.algorithm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DiffListTrivialTest {

	@Test
	public void shouldBuildDiffWhenSequenceEmpty() {
		List<String> left = Arrays.asList();
		List<String> right = Arrays.asList();
		DiffListTrivial<String> diffList = new DiffListTrivial<String>(left, right);
		List<DiffListItem<String>> result = diffList.diff();

		assertThat(result.size(), is(0));
	}

	@Test
	public void shouldBuildDiffWhenSequenceLeftFilled() {
		List<String> left = Arrays.asList("a", "b", "c");
		List<String> right = Arrays.asList();
		DiffListTrivial<String> diffList = new DiffListTrivial<String>(left, right);
		List<DiffListItem<String>> result = diffList.diff();

		assertThat(result.size(), is(3));
		assertThat(result.get(0).getType(), is(DiffType.LEFT));
		assertThat(result.get(1).getType(), is(DiffType.LEFT));
		assertThat(result.get(2).getType(), is(DiffType.LEFT));
		assertThat(result.get(0).getLeft(), is("a"));
		assertThat(result.get(1).getLeft(), is("b"));
		assertThat(result.get(2).getLeft(), is("c"));
		assertThat(result.get(0).getRight(), nullValue());
		assertThat(result.get(1).getRight(), nullValue());
		assertThat(result.get(2).getRight(), nullValue());
	}

	@Test
	public void shouldBuildDiffWhenSequenceRightFilled() {
		List<String> left = Arrays.asList();
		List<String> right = Arrays.asList("a", "b", "c");
		DiffListTrivial<String> diffList = new DiffListTrivial<String>(left, right);
		List<DiffListItem<String>> result = diffList.diff();

		assertThat(result.size(), is(3));
		assertThat(result.get(0).getType(), is(DiffType.RIGHT));
		assertThat(result.get(1).getType(), is(DiffType.RIGHT));
		assertThat(result.get(2).getType(), is(DiffType.RIGHT));
		assertThat(result.get(0).getLeft(), nullValue());
		assertThat(result.get(1).getLeft(), nullValue());
		assertThat(result.get(2).getLeft(), nullValue());
		assertThat(result.get(0).getRight(), is("a"));
		assertThat(result.get(1).getRight(), is("b"));
		assertThat(result.get(2).getRight(), is("c"));
	}

	@Test
	public void shouldBuildDiffWhenLeftShorten() {
		List<String> left = Arrays.asList("a", "b", "c");
		List<String> right = Arrays.asList("c", "x", "d", "x", "e");
		DiffListTrivial<String> diffList = new DiffListTrivial<String>(left, right);
		List<DiffListItem<String>> result = diffList.diff();

		assertThat(result.size(), is(5));
		assertThat(result.get(0).getType(), is(DiffType.BOTH));
		assertThat(result.get(1).getType(), is(DiffType.BOTH));
		assertThat(result.get(2).getType(), is(DiffType.BOTH));
		assertThat(result.get(3).getType(), is(DiffType.RIGHT));
		assertThat(result.get(4).getType(), is(DiffType.RIGHT));
		assertThat(result.get(0).getLeft(), is("a"));
		assertThat(result.get(1).getLeft(), is("b"));
		assertThat(result.get(2).getLeft(), is("c"));
		assertThat(result.get(3).getLeft(), nullValue());
		assertThat(result.get(4).getLeft(), nullValue());
		assertThat(result.get(0).getRight(), is("c"));
		assertThat(result.get(1).getRight(), is("x"));
		assertThat(result.get(2).getRight(), is("d"));
		assertThat(result.get(3).getRight(), is("x"));
		assertThat(result.get(4).getRight(), is("e"));
	}

	@Test
	public void shouldBuildDiffWhenRightShorten() {
		List<String> left = Arrays.asList("a", "b", "c", "d", "e");
		List<String> right = Arrays.asList("c", "x", "d");
		DiffListTrivial<String> diffList = new DiffListTrivial<String>(left, right);
		List<DiffListItem<String>> result = diffList.diff();

		assertThat(result.size(), is(5));
		assertThat(result.get(0).getType(), is(DiffType.BOTH));
		assertThat(result.get(1).getType(), is(DiffType.BOTH));
		assertThat(result.get(2).getType(), is(DiffType.BOTH));
		assertThat(result.get(3).getType(), is(DiffType.LEFT));
		assertThat(result.get(4).getType(), is(DiffType.LEFT));
		assertThat(result.get(0).getLeft(), is("a"));
		assertThat(result.get(1).getLeft(), is("b"));
		assertThat(result.get(2).getLeft(), is("c"));
		assertThat(result.get(3).getLeft(), is("d"));
		assertThat(result.get(4).getLeft(), is("e"));
		assertThat(result.get(0).getRight(), is("c"));
		assertThat(result.get(1).getRight(), is("x"));
		assertThat(result.get(2).getRight(), is("d"));
		assertThat(result.get(3).getRight(), nullValue());
		assertThat(result.get(4).getRight(), nullValue());
	}

}
