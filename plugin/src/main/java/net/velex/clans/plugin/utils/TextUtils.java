package net.velex.clans.plugin.utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TextUtils {
	private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
	
	static {
		suffixes.put(1_000L, "k");
		suffixes.put(1_000_000L, "M");
		suffixes.put(1_000_000_000L, "G");
		suffixes.put(1_000_000_000_000L, "T");
		suffixes.put(1_000_000_000_000_000L, "P");
		suffixes.put(1_000_000_000_000_000_000L, "E");
	}
	
	public static @NotNull String prefixFormatted(final long value) {
		// Checks if the value given is equal than the lower value for Long type.
		if (value == Integer.MIN_VALUE) {
      return prefixFormatted(Integer.MIN_VALUE + 1);
    }
    // The value is lower than zero?
		if (value < 0) {
      return "-" + prefixFormatted(-value);
    }
    // The value is lower than 1000?
		if (value < 1000) {
      return Long.toString(value); //deal with easy case
    }
		final var entry = suffixes.floorEntry(value);
		final var suffix = entry.getValue();
		final var truncated = value / (entry.getKey() / 10); //the number part of the output times 10
    final var decimal = truncated < 100 && (truncated / 10d) != ((float) truncated / 10);
    return decimal
			? (truncated / 10d) + suffix
			: (truncated / 10) + suffix;
  }
	
	public static @NotNull String year() {
		return Integer.toString(Year.now().getValue());
	}
	
	public static @NotNull String month() {
		return new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime());
	}
}
