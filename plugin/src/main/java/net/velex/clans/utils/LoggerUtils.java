package net.velex.clans.utils;

import net.velex.clans.ClansPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtils {
  private static final Logger LOGGER = ClansPlugin.getPlugin(ClansPlugin.class).getLogger();
  
  public static void info(final String @NotNull... content) {
    for (final var message : content) {
      LOGGER.log(Level.INFO, message);
    }
  }
  
  public static void warn(final String @NotNull... content) {
    for (final var message : content) {
      LOGGER.log(Level.WARNING, message);
    }
  }
  
  public static void error(final String @NotNull... content) {
    for (final var message : content) {
      LOGGER.log(Level.SEVERE, message);
    }
  }
}
