package net.velex.clans.utils;

import com.aivruu.iridiumcolorapi.ColorAPI;
import com.aivruu.iridiumcolorapi.model.ColorAPIModel;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlaceholderUtils {
  private static final ColorAPIModel MODEL = new ColorAPI();
  
  private static boolean placeholderApiAvailable;
  
  public static void setPlaceholderApiAvailable(final boolean placeholderApiAvailable) {
    PlaceholderUtils.placeholderApiAvailable = placeholderApiAvailable;
  }

  public static @NotNull String colorize(final @NotNull String text) {
    return MODEL.process(text);
  }
  
  public static @NotNull List<String> colorize(final @NotNull List<String> content) {
    return MODEL.process(content);
  }
  
  public static @NotNull String parse(final @NotNull Player player, final @NotNull String text) {
    return placeholderApiAvailable
      ? MODEL.process(PlaceholderAPI.setPlaceholders(player, text))
      : MODEL.process(text);
  }
  
  public static @NotNull List<String> parse(final @NotNull Player player, final @NotNull List<String> content) {
    return placeholderApiAvailable
      ? MODEL.process(PlaceholderAPI.setPlaceholders(player, content))
      : MODEL.process(content);
  }
}
