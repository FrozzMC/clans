package net.velex.clans.api;

import net.velex.clans.api.clan.ClanChatManager;
import net.velex.clans.api.clan.ClanManager;
import net.velex.clans.api.config.ConfigManager;
import net.velex.clans.api.hook.HookManager;
import net.velex.clans.api.model.*;
import org.jetbrains.annotations.NotNull;

public interface Clans {
  @NotNull ConfigManager configManager();
  
  @NotNull ClanManager clanManager();
  
  @NotNull ClanChatManager clanChatManager();
  
  @NotNull HookManager hookManager();

  @NotNull AntiFarmManagerModel antiFarmManagerModel();
}
