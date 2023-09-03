package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.clan.ClanModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanDeleteEvent extends ClanEvent {
  private final Player player;
  private final DeleteCause cause;
  
  public ClanDeleteEvent(
    final @NotNull ClanModel clanModel,
    final @NotNull Player player,
    final @NotNull DeleteCause cause
  ) {
    super(clanModel);
    this.player = Preconditions.checkNotNull(player, "Player reference cannot be null.");
    this.cause = Preconditions.checkNotNull(cause, "Cause type reference cannot be null.");
  }
  
  public @NotNull Player player() {
    return player;
  }
  
  public @NotNull DeleteCause cause() {
    return cause;
  }
  
  public enum DeleteCause {
    /**
     * Indicates that the clan was deleted by the plugin,
     */
    DELETE_BY_API,
    /**
     * Indicates that the clan was deleted by an administrator.
     */
    DELETE_BY_ADMIN,
    /**
     * Indicates that the clan was deleted by the leader.
     */
    DELETE_BY_LEADER
  }
}
