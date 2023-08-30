package net.velex.clans.api.model;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public interface ClanChatHandlerModel {
  void handle(final @NotNull AsyncPlayerChatEvent event);
  
  void performClanMessage(final @NotNull String id, final @NotNull String message);
  
  void performAllyMessage(final @NotNull String id, final @NotNull String message);
  
  void performTruceMessage(final @NotNull String id, final @NotNull String message);
}
