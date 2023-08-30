package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.enums.Cause;
import net.velex.clans.api.model.internal.ClanDataInternalModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanJoinEvent extends ClanEvent {
  private final Player player;
  private final Cause cause;
  
  public ClanJoinEvent(
    final @NotNull Player player,
    final @NotNull ClanDataInternalModel clanDataInternalModel,
    final @NotNull Cause cause
  ) {
    super(clanDataInternalModel);
    this.player = Preconditions.checkNotNull(player, "Player reference cannot be null.");
    this.cause = Preconditions.checkNotNull(cause, "Cause type cannot be null.");
  }
  
  public @NotNull Player player() {
    return player;
  }
  
  public @NotNull Cause cause() {
    return cause;
  }
}
