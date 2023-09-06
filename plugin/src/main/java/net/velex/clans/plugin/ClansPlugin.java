package net.velex.clans.plugin;

import net.velex.clans.api.Clans;
import net.velex.clans.api.Provider;
import net.velex.clans.api.antifarm.AntiFarmManager;
import net.velex.clans.api.clan.ClanChatManager;
import net.velex.clans.api.clan.ClanManager;
import net.velex.clans.api.config.ConfigManager;
import net.velex.clans.api.hook.HookManager;
import net.velex.clans.plugin.utils.LoggerUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class ClansPlugin extends JavaPlugin implements Clans {
  private ConfigManager configManager;
  private HookManager hookManager;
  private ClanManager clanManager;
  private ClanChatManager clanChatManager;
  private AntiFarmManager antiFarmManager;

  @Override
  public void onLoad() {
    Provider.load(this);
    
    LoggerUtils.info("Loading configuration components for the plugin...");
    configManager = new ConfigManager(getDataFolder().toPath());
    // Checks if some configuration model were not loaded correctly.
    if (configManager.load() == ConfigManager.Result.NO_CONFIG_LOAD) {
      LoggerUtils.error("Some configuration files could not be loaded correctly.");
      setEnabled(false);
      return;
    }
    clanManager = new ClanManager();
    hookManager = new HookManager(getServer().getPluginManager());
    clanChatManager = new ClanChatManager(clanManager, configManager.config());
    antiFarmManager = new AntiFarmManager();
  }
  
  @Override
  public void onEnable() {
    // Checks if the 'hook()' method execution result is a 'NO_HOOKED' enum type.
    if (hookManager.hook() == HookManager.Result.NO_HOOKED) {
      LoggerUtils.error("Some dependency could not be hooked by the plugin, check if they are installed and update.");
    }
    clanManager.loadClansData();
  }
  
  @Override
  public void onDisable() {
    Provider.unload();
    
    LoggerUtils.info(
      "Disabling plugin internal components...",
      "Clearing cache memory used by the plugin..."
    );
    // Checks if the ClanManagerModel reference has not been deleted.
    if (clanManager != null) {
      clanManager.clear();
    }
  }
  
  @Override
  public @NotNull ConfigManager configManager() {
    return configManager;
  }
  
  @Override
  public @NotNull ClanManager clanManager() {
    return clanManager;
  }
  
  @Override
  public @NotNull ClanChatManager clanChatManager() {
    return clanChatManager;
  }
  
  @Override
  public @NotNull HookManager hookManager() {
    return hookManager;
  }
  
  @Override
  public @NotNull AntiFarmManager antiFarmManager() {
    return antiFarmManager;
  }
}
