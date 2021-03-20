package ru.snake.jdbc.diff.algorithm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DiffListClassicTest {

	@Test
	public void shouldBuildDiffWhenSequenceEmpty() {
		List<String> left = Arrays.asList();
		List<String> right = Arrays.asList();
		DiffListClassic<String> diffList = new DiffListClassic<String>(left, right, Object::equals);
		List<DiffListItem<String>> result = diffList.diff();

		assertThat(result.size(), is(0));
	}

	@Test
	public void shouldBuildDiffWhenSequenceLeftFilled() {
		List<String> left = Arrays.asList("a", "b", "c");
		List<String> right = Arrays.asList();
		DiffListClassic<String> diffList = new DiffListClassic<String>(left, right, Object::equals);
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
		DiffListClassic<String> diffList = new DiffListClassic<String>(left, right, Object::equals);
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
	public void shouldBuildDiffWhenSequenceInTail() {
		List<String> left = Arrays.asList("a", "b", "c", "d", "e");
		List<String> right = Arrays.asList("c", "x", "d", "x", "e");
		DiffListClassic<String> diffList = new DiffListClassic<String>(left, right, Object::equals);
		List<DiffListItem<String>> result = diffList.diff();

		assertThat(result.size(), is(7));
		assertThat(result.get(0).getType(), is(DiffType.LEFT));
		assertThat(result.get(1).getType(), is(DiffType.LEFT));
		assertThat(result.get(2).getType(), is(DiffType.BOTH));
		assertThat(result.get(3).getType(), is(DiffType.RIGHT));
		assertThat(result.get(4).getType(), is(DiffType.BOTH));
		assertThat(result.get(5).getType(), is(DiffType.RIGHT));
		assertThat(result.get(6).getType(), is(DiffType.BOTH));
		assertThat(result.get(0).getLeft(), is("a"));
		assertThat(result.get(1).getLeft(), is("b"));
		assertThat(result.get(2).getLeft(), is("c"));
		assertThat(result.get(3).getLeft(), nullValue());
		assertThat(result.get(4).getLeft(), is("d"));
		assertThat(result.get(5).getLeft(), nullValue());
		assertThat(result.get(6).getLeft(), is("e"));
		assertThat(result.get(0).getRight(), nullValue());
		assertThat(result.get(1).getRight(), nullValue());
		assertThat(result.get(2).getRight(), is("c"));
		assertThat(result.get(3).getRight(), is("x"));
		assertThat(result.get(4).getRight(), is("d"));
		assertThat(result.get(5).getRight(), is("x"));
		assertThat(result.get(6).getRight(), is("e"));
	}

	@Test
	public void shouldBuildDiffWhenSequenceInHead() {
		List<String> left = Arrays.asList("a", "b", "c", "d", "e");
		List<String> right = Arrays.asList("a", "x", "b", "x", "c");
		DiffListClassic<String> diffList = new DiffListClassic<String>(left, right, Object::equals);
		List<DiffListItem<String>> result = diffList.diff();

		assertThat(result.size(), is(7));
		assertThat(result.get(0).getType(), is(DiffType.BOTH));
		assertThat(result.get(1).getType(), is(DiffType.RIGHT));
		assertThat(result.get(2).getType(), is(DiffType.BOTH));
		assertThat(result.get(3).getType(), is(DiffType.RIGHT));
		assertThat(result.get(4).getType(), is(DiffType.BOTH));
		assertThat(result.get(5).getType(), is(DiffType.LEFT));
		assertThat(result.get(6).getType(), is(DiffType.LEFT));
		assertThat(result.get(0).getLeft(), is("a"));
		assertThat(result.get(1).getLeft(), nullValue());
		assertThat(result.get(2).getLeft(), is("b"));
		assertThat(result.get(3).getLeft(), nullValue());
		assertThat(result.get(4).getLeft(), is("c"));
		assertThat(result.get(5).getLeft(), is("d"));
		assertThat(result.get(6).getLeft(), is("e"));
		assertThat(result.get(0).getRight(), is("a"));
		assertThat(result.get(1).getRight(), is("x"));
		assertThat(result.get(2).getRight(), is("b"));
		assertThat(result.get(3).getRight(), is("x"));
		assertThat(result.get(4).getRight(), is("c"));
		assertThat(result.get(5).getRight(), nullValue());
		assertThat(result.get(6).getRight(), nullValue());
	}

}
