package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.model.internal.ClanDataInternalModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanLeaveEvent extends ClanEvent {
  private final Player player;
  
  public ClanLeaveEvent(final @NotNull ClanDataInternalModel clanDataInternalModel, final @NotNull Player player) {
    super(clanDataInternalModel);
    this.player = Preconditions.checkNotNull(player, "Player reference cannot be null.");
  }
  
  public @NotNull Player player() {
    return player;
  }
}
