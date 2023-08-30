package net.velex.clans.plugin.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.velex.clans.plugin.Constants;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClanExpansionHook extends PlaceholderExpansion {
  @Override
  public boolean canRegister() {
    return true;
  }
  
  @Override
  public @NotNull String getIdentifier() {
    return "clans";
  }
  
  @Override
  public @NotNull String getAuthor() {
    return "Qekly";
  }
  
  @Override
  public @NotNull String getVersion() {
    return Constants.VERSION;
  }
  
  @Override
  public @Nullable String onPlaceholderRequest(final @Nullable Player player, final @NotNull String params) {
    if (player == null) {
      return "";
    }
    
    return null;
  }
}
