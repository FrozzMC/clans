package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.clan.ClanModel;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ClanEvent extends Event implements Cancellable {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  
  private final ClanModel clanModel;

  private boolean cancelled;
  
  public ClanEvent(final @NotNull ClanModel clanModel) {
    this.clanModel = Preconditions.checkNotNull(clanModel, "ClanModel reference cannot be null.");
  }
  
  public @NotNull ClanModel clanModel() {
    return clanModel;
  }
  
  @Override
  public boolean isCancelled() {
    return cancelled;
  }
  
  @Override
  public void setCancelled(final boolean cancelled) {
    this.cancelled = cancelled;
  }
  
  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }
  
  public static @NotNull HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
