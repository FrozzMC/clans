package net.velex.clans.utils.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class LocationUtils {
	private static final StringBuilder BUILDER = new StringBuilder();
	
	public static @NotNull Location from(final @NotNull String str) {
		final var split = str.split(";");
    // Checks if the array length is equal to six.
		if (split.length == 6) {
			return new Location(
				Bukkit.getWorld(split[0]),
				Double.parseDouble(split[1]),
				Double.parseDouble(split[2]),
				Double.parseDouble(split[3]),
				Float.parseFloat(split[4]),
				Float.parseFloat(split[5])
			);
		}
		return new Location(
			Bukkit.getWorld(split[0]),
			Double.parseDouble(split[1]),
			Double.parseDouble(split[2]),
			Double.parseDouble(split[3])
		);
	}

	public static @NotNull String to(final @NotNull Location loc, final boolean yawPitch) {
    final var world = loc.getWorld();
    // Checks if the location world is null.
    if (world == null) {
      return "";
    }
    // Checks if the 'yawPitch' value is true.
		if (yawPitch) {
			return BUILDER.append(world.getName())
				.append(";")
				.append(loc.getX())
				.append(";")
				.append(loc.getY())
				.append(";")
				.append(loc.getZ())
				.append(";")
				.append(loc.getYaw())
				.append(";")
				.append(loc.getPitch())
				.toString();
		}
		return BUILDER.append(world.getName())
			.append(";")
			.append(loc.getX())
			.append(";")
			.append(loc.getY())
			.append(";")
			.append(loc.getZ())
			.toString();
	}
}
