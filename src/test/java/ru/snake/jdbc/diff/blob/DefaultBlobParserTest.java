package ru.snake.jdbc.diff.blob;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class DefaultBlobParserTest {

	@Test
	public void shouldBuildObjectWhenSizeIsZero() {
		byte[] blob = new byte[] {};
		Object result = new DefaultBlobParser().parse(blob);

		assertThat(result, notNullValue());
		assertThat(result, instanceOf(Map.class));

		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) result;

		assertThat(resultMap.get("length"), is(0));
		assertThat(resultMap.get("data"), instanceOf(List.class));

		@SuppressWarnings("unchecked")
		List<String> dataList = (List<String>) resultMap.get("data");

		assertThat(dataList.size(), is(0));
	}

	@Test
	public void shouldBuildObjectWhenSizeIsSmall() {
		byte[] blob = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
		Object result = new DefaultBlobParser().parse(blob);

		assertThat(result, notNullValue());
		assertThat(result, instanceOf(Map.class));

		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) result;

		assertThat(resultMap.get("length"), is(8));
		assertThat(resultMap.get("data"), instanceOf(List.class));

		@SuppressWarnings("unchecked")
		List<String> dataList = (List<String>) resultMap.get("data");

		assertThat(dataList.size(), is(1));
		assertThat(dataList.get(0), is("00010203 04050607 "));
	}

	@Test
	public void shouldBuildObjectWhenSizeIsRow() {
		byte[] blob = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
		Object result = new DefaultBlobParser().parse(blob);

		assertThat(result, notNullValue());
		assertThat(result, instanceOf(Map.class));

		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) result;

		assertThat(resultMap.get("length"), is(16));
		assertThat(resultMap.get("data"), instanceOf(List.class));

		@SuppressWarnings("unchecked")
		List<String> dataList = (List<String>) resultMap.get("data");

		assertThat(dataList.size(), is(1));
		assertThat(dataList.get(0), is("00010203 04050607 08090a0b 0c0d0e0f "));
	}

	@Test
	public void shouldBuildObjectWhenSizeIsRowAndHalf() {
		byte[] blob = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 };
		Object result = new DefaultBlobParser().parse(blob);

		assertThat(result, notNullValue());
		assertThat(result, instanceOf(Map.class));

		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) result;

		assertThat(resultMap.get("length"), is(19));
		assertThat(resultMap.get("data"), instanceOf(List.class));

		@SuppressWarnings("unchecked")
		List<String> dataList = (List<String>) resultMap.get("data");

		assertThat(dataList.size(), is(2));
		assertThat(dataList.get(0), is("00010203 04050607 08090a0b 0c0d0e0f "));
		assertThat(dataList.get(1), is("101112"));
	}

	@Test
	public void shouldBuildObjectWhenSizeIsTooLarge() {
		byte[] blob = new byte[140];
		Object result = new DefaultBlobParser().parse(blob);

		assertThat(result, notNullValue());
		assertThat(result, instanceOf(Map.class));

		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) result;

		assertThat(resultMap.get("length"), is(140));
		assertThat(resultMap.get("data"), instanceOf(List.class));

		@SuppressWarnings("unchecked")
		List<String> dataList = (List<String>) resultMap.get("data");

		assertThat(dataList.size(), is(9));
		assertThat(dataList.get(0), is("00000000 00000000 00000000 00000000 "));
		assertThat(dataList.get(1), is("00000000 00000000 00000000 00000000 "));
		assertThat(dataList.get(6), is("00000000 00000000 00000000 00000000 "));
		assertThat(dataList.get(7), is("00000000 00000000 00000000 00000000 "));
		assertThat(dataList.get(8), is("..."));
	}

}
