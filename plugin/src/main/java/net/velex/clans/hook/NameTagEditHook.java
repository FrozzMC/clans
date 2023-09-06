package net.velex.clans.hook;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.INametagApi;
import net.velex.clans.event.ClanJoinEvent;
import net.velex.clans.event.ClanKickEvent;
import net.velex.clans.event.ClanLeaveEvent;
import net.velex.clans.event.ClanRenameEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class NameTagEditHook implements Listener {
  private final INametagApi api;
  
  public NameTagEditHook() {
    api = NametagEdit.getApi();
  }
  
  @EventHandler (priority = EventPriority.LOW)
  public void onClanRename(final @NotNull ClanRenameEvent event) {
    for (OfflinePlayer member : event.clanInternalModel().members()) {
      // Checks if there are a player with that exact name.
      if (Bukkit.getPlayerExact(member.getName()) == null) {
        continue;
      }
      api.reloadNametag((Player) member);
      member = null;
    }
  }
  
  @EventHandler (priority = EventPriority.LOW)
  public void onClanJoin(final @NotNull ClanJoinEvent event) {
    api.reloadNametag(event.player());
  }
  
  @EventHandler (priority = EventPriority.LOW)
  public void onClanLeave(final @NotNull ClanLeaveEvent event) {
    api.reloadNametag(event.player());
    for (OfflinePlayer member : event.clanInternalModel().members()) {
      // Checks if there are a player with that exact name.
      if (Bukkit.getPlayerExact(member.getName()) == null) {
        continue;
      }
      api.reloadNametag((Player) member);
      member = null;
    }
  }
  
  @EventHandler (priority = EventPriority.LOW)
  public void onClanKick(final @NotNull ClanKickEvent event) {
    final var kickedPlayer = event.kickedPlayer();
    // Checks if there are a player with that exact name.
    if (!kickedPlayer.isOnline()) {
      return;
    }
    api.reloadNametag(kickedPlayer);
  }
}
