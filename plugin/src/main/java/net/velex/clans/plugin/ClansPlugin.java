package net.velex.clans.plugin;

import net.velex.clans.api.Clans;
import net.velex.clans.api.Provider;
import net.velex.clans.api.enums.Result;
import net.velex.clans.api.model.*;
import net.velex.clans.plugin.impl.ClanChatHandlerImpl;
import net.velex.clans.plugin.impl.SimpleAntiFarmManagerImpl;
import net.velex.clans.plugin.impl.SimpleConfManagerModelImpl;
import net.velex.clans.plugin.impl.SimpleHookManagerImpl;
import net.velex.clans.plugin.utils.LoggerUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class ClansPlugin extends JavaPlugin implements Clans {
  private ConfManagerModel confManagerModel;
  private HookManagerModel hookManagerModel;
  private ClanManagerModel clanManagerModel;
  private ClanTopManagerModel clanTopManagerModel;
  private ClanChatHandlerModel clanChatHandlerModel;
  private AntiFarmManagerModel antiFarmManagerModel;

  @Override
  public @NotNull ConfManagerModel confManagerModel() {
    return confManagerModel;
  }
  
  @Override
  public @NotNull ClanManagerModel clanManagerModel() {
    return clanManagerModel;
  }
  
  @Override
  public @NotNull ClanTopManagerModel clanTopManagerModel() {
    return clanTopManagerModel;
  }
  
  @Override
  public @NotNull ClanChatHandlerModel clanChatHandlerModel() {
    return clanChatHandlerModel;
  }
  
  @Override
  public @NotNull HookManagerModel hookManagerModel() {
    return hookManagerModel;
  }

  @Override
  public @NotNull AntiFarmManagerModel antiFarmManagerModel() {
    return antiFarmManagerModel;
  }

  @Override
  public void onLoad() {
    Provider.load(this);
    
    LoggerUtils.info("Loading configuration components for the plugin...");
    confManagerModel = new SimpleConfManagerModelImpl(getDataFolder().toPath());
    // Checks if some configuration model were not loaded correctly.
    if (confManagerModel.load() == Result.NO_CONFIG_LOAD) {
      LoggerUtils.error("Some configuration files could not be loaded correctly.");
      setEnabled(false);
      return;
    }
    hookManagerModel = new SimpleHookManagerImpl(getServer().getPluginManager());
    clanChatHandlerModel = new ClanChatHandlerImpl(clanManagerModel);
    antiFarmManagerModel = new SimpleAntiFarmManagerImpl();
  }
  
  @Override
  public void onEnable() {
    // Checks if the 'hook()' method execution result is a 'NO_HOOKED' enum type.
    if (hookManagerModel.hook() == Result.NO_HOOKED) {
      LoggerUtils.error("Some dependency could not be hooked by the plugin, check if they are installed and update.");
    }
  }
  
  @Override
  public void onDisable() {
    Provider.unload();
    
    LoggerUtils.info(
      "Disabling plugin internal components...",
      "Clearing cache memory used by the plugin..."
    );
    // Checks if the ClansManagerModel reference has not been deleted.
    if (clanManagerModel != null) {
      clanManagerModel.clear();
    }
    // Checks if the ClanTopManagerModel reference has not been deleted.
    if (clanTopManagerModel != null) {
      clanTopManagerModel.clear();
    }
  }
}
