package net.velex.clans.event;

import com.google.common.base.Preconditions;
import net.velex.clans.clan.ClanModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanLeaveEvent extends ClanEvent {
  private final Player player;
  
  public ClanLeaveEvent(final @NotNull ClanModel clanModel, final @NotNull Player player) {
    super(clanModel);
    this.player = Preconditions.checkNotNull(player, "Player reference cannot be null.");
  }
  
  public @NotNull Player player() {
    return player;
  }
}
