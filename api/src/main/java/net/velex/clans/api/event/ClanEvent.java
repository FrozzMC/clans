package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.model.internal.ClanDataInternalModel;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ClanEvent extends Event implements Cancellable {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  
  private final ClanDataInternalModel clanDataInternalModel;

  private boolean cancelled;
  
  public ClanEvent(final @NotNull ClanDataInternalModel clanDataInternalModel) {
    this.clanDataInternalModel = Preconditions.checkNotNull(clanDataInternalModel, "ClanDataInternalModel reference cannot be null.");
  }
  
  public @NotNull ClanDataInternalModel clanInternalModel() {
    return clanDataInternalModel;
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
