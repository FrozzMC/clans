package net.velex.clans.plugin.impl;

import com.google.common.base.Preconditions;
import net.velex.clans.api.enums.Result;
import net.velex.clans.api.model.HookManagerModel;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class SimpleHookManagerImpl implements HookManagerModel {
  private final PluginManager pluginManager;
  
  public SimpleHookManagerImpl(final @NotNull PluginManager pluginManager) {
    this.pluginManager = Preconditions.checkNotNull(pluginManager, "PluginManager reference cannot be null.");
  }
  
  @Override
  public @NotNull Result hook() {
    // Checks if PlaceholderAPI is available on the server.
    if ((pluginManager.getPlugin("PlaceholderAPI") == null) || !pluginManager.isPluginEnabled("PlaceholderAPI")) {
      return Result.NO_HOOKED;
    }
    // Checks if NameTagEdit is available.
    if ((pluginManager.getPlugin("NameTagEdit") == null) || !pluginManager.isPluginEnabled("NameTagEdit")) {
      return Result.NO_HOOKED;
    }
    // Checks if the WorldGuard dependency is installed.
    if ((pluginManager.getPlugin("WorldGuard") == null) || !pluginManager.isPluginEnabled("WorldGuard")) {
      return Result.NO_HOOKED;
    }
    return Result.SUCCESS;
  }
}
