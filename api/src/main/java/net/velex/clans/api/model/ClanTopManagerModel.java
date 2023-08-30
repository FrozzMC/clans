package net.velex.clans.api.model;

import net.velex.clans.api.model.internal.ClanDataInternalModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ClanTopManagerModel {
  @Nullable ClanDataInternalModel find(final int position);
  
  @NotNull Map<String, ClanDataInternalModel> sortedClans();
  
  void setPosition(final @NotNull ClanDataInternalModel clanModel, final int position);
  
  void clear();
}
