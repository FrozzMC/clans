package net.velex.clans.plugin.impl;

import com.google.common.base.Preconditions;
import net.velex.clans.api.model.ClanChatHandlerModel;
import net.velex.clans.api.model.ClanManagerModel;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ClanChatHandlerImpl implements ClanChatHandlerModel {
  private final ClanManagerModel clanManagerModel;
  
  public ClanChatHandlerImpl(final @NotNull ClanManagerModel clanManagerModel) {
    this.clanManagerModel = Preconditions.checkNotNull(clanManagerModel, "ClanManagerModel reference cannot be null.");
  }
  
  @Override
  public void handle(final @NotNull AsyncPlayerChatEvent event) {
  
  }
  
  @Override
  public void performClanMessage(final @NotNull String id, final @NotNull String message) {
  
  }
  
  @Override
  public void performAllyMessage(final @NotNull String id, final @NotNull String message) {
  
  }
  
  @Override
  public void performTruceMessage(final @NotNull String id, final @NotNull String message) {
  
  }
}
