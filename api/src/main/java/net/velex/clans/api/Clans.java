package net.velex.clans.api;

import net.velex.clans.antifarm.AntiFarmManager;
import net.velex.clans.clan.ClanChatManager;
import net.velex.clans.clan.ClanManager;
import net.velex.clans.config.ConfigManager;
import net.velex.clans.hook.HookManager;
import org.jetbrains.annotations.NotNull;

public interface Clans {
  @NotNull ConfigManager configManager();
  
  @NotNull ClanManager clanManager();
  
  @NotNull ClanChatManager clanChatManager();
  
  @NotNull HookManager hookManager();

  @NotNull AntiFarmManager antiFarmManager();
}
