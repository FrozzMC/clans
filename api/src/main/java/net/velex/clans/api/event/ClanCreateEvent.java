package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.clan.ClanModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanCreateEvent extends ClanEvent {
  private final Player player;
  private final String id;
  private final CreationCause cause;
  
  public ClanCreateEvent(
    final @NotNull ClanModel clanModel,
    final @NotNull Player player,
    final @NotNull String id,
    final @NotNull CreationCause cause
  ) {
    super(clanModel);
    this.player = Preconditions.checkNotNull(player, "Player reference cannot be null.");
    this.id = Preconditions.checkNotNull(id, "Clan ID cannot be null.");
    this.cause = Preconditions.checkNotNull(cause, "CreationCause enum constant cannot be null.");
  }
  
  public @NotNull Player player() {
    return player;
  }
  
  public @NotNull String id() {
    return id;
  }
  
  public @NotNull CreationCause cause() {
    return cause;
  }

  public enum CreationCause {
    /**
     * Indicates that the clan was created by a player.
     */
    CREATION_BY_PLAYER,
    /**
     * Indicates that the clan was created by the plugin.
     */
    CREATION_BY_API
  }
}
