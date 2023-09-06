package net.velex.clans.utils;

import org.jetbrains.annotations.NotNull;

public class TimeUtils {
	private static final StringBuilder BUILDER = new StringBuilder();

  public static @NotNull String format(final int seconds) {
    String formatted;
    // Checks if the seconds amount is higher or equal than 86400.
    // Or, check if the amount is higher or equal than 3600.
    if (seconds >= 86400) {
      formatted = BUILDER.append(Math.floor(seconds / 86400.0D))
        .append(" days ")
        .append(toHours(seconds))
        .append(" hours ")
        .append(toMin(seconds))
        .append(" min")
        .toString();
    } else if (seconds >= 3600) {
      formatted = BUILDER.append(toHours(seconds))
        .append(" hours ")
        .append(toMin(seconds))
        .append(" min ")
        .append(toSec(seconds))
        .append(" sec")
        .toString();
    } else {
      formatted = BUILDER.append(toMin(seconds))
        .append(" min ")
        .append(toSec(seconds))
        .append(" sec")
        .toString();
    }
    return formatted;
  }

	private static short toSec(final float total) {
		final var temp = ((total / 86400.0F - toDays(total)) * 24.0F - toHours(total)) * 60 - toMin(total);
		return (short) Math.floor(temp * 60);
	}
  private static float toMin(final float total) {
		final var temp = (total / 86400.0F - toDays(total)) * 24.0F - toHours(total);
		return (float) Math.floor(temp * 60.0F);
	}
	private static float toHours(final float total) {
		final var temp = total / 86400.0F - toDays(total);
		return (float) Math.floor(temp * 24.0F);
	}
	
	private static double toDays(final float total) {
    return Math.floor(total / 86400.0F);
	}
}
