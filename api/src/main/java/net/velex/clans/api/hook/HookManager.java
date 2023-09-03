package net.velex.clans.api.hook;

import com.google.common.base.Preconditions;
import net.velex.clans.api.enums.Result;
import net.velex.clans.api.hook.guard.WorldGuardHook;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class HookManager {
  private final PluginManager pluginManager;
  
  private WorldGuardHook worldGuardHook;
  
  public HookManager(final @NotNull PluginManager pluginManager) {
    this.pluginManager = Preconditions.checkNotNull(pluginManager, "PluginManager reference cannot be null.");
  }
  
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
  
  public @NotNull WorldGuardHook worldGuardHook() {
    if (worldGuardHook == null) {
      throw new IllegalStateException("Could not be get the WorldGuardHook reference.");
    }
    return worldGuardHook;
  }
}
