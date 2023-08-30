package net.velex.clans.api.event;

import com.google.common.base.Preconditions;
import net.velex.clans.api.enums.Upgrade;
import net.velex.clans.api.model.internal.ClanDataInternalModel;
import org.jetbrains.annotations.NotNull;

public class ClanUpgradeEvent extends ClanEvent {
  private final short level;
  private final Upgrade upgradeType;
  
  public ClanUpgradeEvent(final @NotNull ClanDataInternalModel clanDataInternalModel, final short level, final @NotNull Upgrade upgradeType) {
    super(clanDataInternalModel);
    this.level = level;
    this.upgradeType = Preconditions.checkNotNull(upgradeType, "Upgrade enum type cannot be null.");
  }
  
  public short level() {
    return level;
  }
  
  public @NotNull Upgrade upgradeType() {
    return upgradeType;
  }
}
