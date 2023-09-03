package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.clan.ClanModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanJoinEvent extends ClanEvent {
  private final Player player;
  
  public ClanJoinEvent(
    final @NotNull ClanModel clanModel,
    final @NotNull Player player
  ) {
    super(clanModel);
    this.player = Preconditions.checkNotNull(player, "Player reference cannot be null.");
  }
  
  public @NotNull Player player() {
    return player;
  }
}
