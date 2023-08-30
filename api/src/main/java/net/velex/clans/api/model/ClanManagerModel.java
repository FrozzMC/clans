package net.velex.clans.api.model;

import net.velex.clans.api.model.internal.ClanDataInternalModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClanManagerModel {
  boolean areAllies(final @NotNull ClanDataInternalModel clanAlphaModel, final @NotNull ClanDataInternalModel clanBravoModel);
  
  boolean areTruces(final @NotNull ClanDataInternalModel clanAlphaModel, final @NotNull ClanDataInternalModel clanBravoModel);
  
  void removeAllies(final @NotNull ClanDataInternalModel clanAlphaModel, final @NotNull ClanDataInternalModel clanBravoModel);
  
  void removeTruces(final @NotNull ClanDataInternalModel clanAlphaModel, final @NotNull ClanDataInternalModel clanBravoModel);

  @NotNull ClanDataInternalModel create(
    final @NotNull String leaderID,
    final @NotNull String clanName,
    final boolean bypassRequirements
  );
  
  boolean isClanLeader(final @NotNull String leaderID);
  
  boolean isClanMember(final @NotNull String leaderID);
  
  boolean existsThatClan(final @NotNull String clanName);
  
  @Nullable ClanDataInternalModel findOrNull(final @NotNull String clanName);
  
  void delete(final @NotNull ClanDataInternalModel clanModel);
  
  void makeLeader(final @NotNull String newLeaderID);
  
  boolean isSpying(final @NotNull String id);
  
  void clear();
}
