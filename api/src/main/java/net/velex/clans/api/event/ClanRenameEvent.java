package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.clan.ClanModel;
import org.jetbrains.annotations.NotNull;

public class ClanRenameEvent extends ClanEvent {
  private final String formerName;
  private final String currentName;
  
  public ClanRenameEvent(
    final @NotNull ClanModel clanModel,
    final @NotNull String formerName,
    final @NotNull String currentName
  ) {
    super(clanModel);
    this.formerName = Preconditions.checkNotNull(formerName, "Clan former name cannot be null.");
    this.currentName = Preconditions.checkNotNull(currentName, "Clan new name cannot be null.");
  }
  
  public @NotNull String formerName() {
    return formerName;
  }
  
  public @NotNull String currentName() {
    return currentName;
  }
}
