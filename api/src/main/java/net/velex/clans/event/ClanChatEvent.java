package net.velex.clans.event;

import com.google.common.base.Preconditions;
import net.velex.clans.clan.ClanModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanChatEvent extends ClanEvent {
  private final Player player;
  private final String content;
  
  public ClanChatEvent(
    final @NotNull ClanModel clanModel,
    final @NotNull Player player,
    final @NotNull String content
  ) {
    super(clanModel);
    this.player = Preconditions.checkNotNull(player, "Player reference cannot be null.");
    this.content = Preconditions.checkNotNull(content, "Message content cannot be null.");
  }
  
  public @NotNull Player player() {
    return player;
  }
  
  public @NotNull String content() {
    return content;
  }
}
