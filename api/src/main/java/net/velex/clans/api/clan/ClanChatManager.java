package net.velex.clans.api.clan;

import com.google.common.base.Preconditions;
import net.velex.clans.api.config.model.ConfModel;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ClanChatManager {
  private final ClanManager clanManager;
  private final ConfModel config;
  
  public ClanChatManager(final @NotNull ClanManager clanManager, final @NotNull ConfModel config) {
    this.clanManager = Preconditions.checkNotNull(clanManager, "ClanManager reference cannot be null.");
    this.config = Preconditions.checkNotNull(config, "ConfModel reference cannot be null.");
  }
  
  public @NotNull Result handleAsync(final @NotNull AsyncPlayerChatEvent event) {
    final var player = event.getPlayer();
    final var id = player.getUniqueId().toString();
    final var clanModel = clanManager.findById(id);
    // Checks if the player is in a clan.
    if (!clanModel.isMember(id)) {
      return Result.NO_PLAYER_CLAN;
    }
    final var message = event.getMessage();
    // Checks if the message is for the player clan.
    if (message.startsWith(config.clanChatSymbol())) {
      event.setCancelled(true);
      return performClanMessage(clanModel, message.substring(0, 1));
    }
    return Result.NO_PERFORM_MESSAGE;
  }
  
  public @NotNull Result performClanMessage(final @NotNull ClanModel clanModel, final @NotNull String message) {
    final var members = clanModel.members();
    // The clan just have one member (leader)?
    if (members.size() == 1) {
      return Result.NO_AVAILABLE_MEMBERS;
    }
    for (final var memberModel : members.values()) {
      final var player = Bukkit.getPlayer(UUID.fromString(memberModel.id()));
      // Checks if the iterated player is connected.
      if (player == null) {
        continue;
      }
      player.sendMessage(config.clanChatFormat()
        .replace("<tag>", memberModel.tag())
        .replace("<player>", player.getName())
        .replace("<message>", message));
    }
    return Result.SUCCESS;
  }
  
  public enum Result {
    /**
     * Indicates that the operation were completed correctly.
     */
    SUCCESS,
    /**
     * Indicates that the message cannot be handled by the manager.
     */
    NO_PERFORM_MESSAGE,
    /**
     * Indicates that the player doesn't have a clan.
     */
    NO_PLAYER_CLAN,
    /**
     * Indicates that the clan doesn't have enough members to send the message.
     */
    NO_AVAILABLE_MEMBERS
  }
}
