package ru.snake.jdbc.diff.blob;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultBlobParser implements BlobParser {

	private static final int MAX_LENGTH = 128;

	private static final int ROW_LENGTH = 16;

	private static final int CELL_LENGTH = 4;

	@Override
	public Object parse(byte[] blob) {
		Map<String, Object> object = new LinkedHashMap<>();
		List<String> data = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		int index = 0;

		while (index < blob.length && index < MAX_LENGTH) {
			byte b = blob[index];
			builder.append(Character.forDigit((b >> 4) & 0x0f, 16));
			builder.append(Character.forDigit((b >> 0) & 0x0f, 16));

			if (index % CELL_LENGTH == CELL_LENGTH - 1) {
				builder.append(' ');
			}

			if (index % ROW_LENGTH == ROW_LENGTH - 1) {
				data.add(builder.toString());

				builder = new StringBuilder();
			}

			index += 1;
		}

		if (builder.length() > 0) {
			data.add(builder.toString());
		}

		if (index == MAX_LENGTH) {
			data.add("...");
		}

		object.put("length", blob.length);
		object.put("data", data);

		return object;
	}

	@Override
	public String toString() {
		return "DefaultBlobParser []";
	}

}
