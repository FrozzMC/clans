package net.velex.clans.api.model.internal;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClanDataInternalModel {
  public short wins() {
    return 0;
  }
  
  public short loses() {
    return 0;
  }
  
  public @NotNull String name() {
    return "";
  }
  
  public @NotNull String rawName() {
    return "";
  }
  
  public void setName(final @NotNull String clanName) {
  
  }
  
  public @NotNull String leaderName() {
    return "";
  }
  
  public @NotNull List<OfflinePlayer> members() {
    return List.of();
  }
}
