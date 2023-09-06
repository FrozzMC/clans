package net.velex.clans.api.clan;

import net.velex.clans.api.clan.base.BaseModel;
import net.velex.clans.api.clan.member.MemberModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ClanManager {
  private final Map<String, ClanModel> clans;
  
  public ClanManager() {
    clans = new HashMap<>();
  }
  
  public void loadClansData() {}
  
  /**
   * Creates a new clan using the parameters/data given, and return a {@link ClanManager.Result} enum type.
   *
   * @param clanId The ID for the clan.
   * @param leader The name of the leader.
   * @param bases An array with the current bases of the clan.
   *
   * @return If the clan is already created will return a {@link ClanManager.Result#ALREADY_CLAN_CREATED} enum type.
   * <p> Else, will return a {@link ClanManager.Result#SUCCESS} enum type.
   */
  public @NotNull Result create(
    final @NotNull String clanId,
    final @NotNull Map<String, MemberModel> members,
    final BaseModel @NotNull[] bases,
    final @NotNull String leader
  ) {
    // Checks if the that clan is already created.
    if (clans.containsKey(clanId)) {
      return Result.ALREADY_CLAN_CREATED;
    }
    clans.put(clanId, new ClanModel(clanId, bases, members, leader));
    return Result.SUCCESS;
  }
  
  /**
   * Checks if the player given is the current leader of the given clan.
   *
   * @param clanModel Clan for the leader check.
   * @param playerId ID of the player.
   *
   * @return Will return true if the player is the leader of the clan. Else, will return false.
   */
  public boolean isLeader(final @NotNull ClanModel clanModel, final @NotNull String playerId) {
    return clanModel.leader().equals(playerId);
  }
  
  /**
   * Checks if the player given is a member of the clan given.
   *
   * @param clanModel Clan for the member search.
   * @param playerId ID of the player.
   *
   * @return This method return true if the player given is a member of the given clan, else, will return false.
   */
  public boolean isMember(final @NotNull ClanModel clanModel, final @NotNull String playerId) {
    for (final var memberModel : clanModel.members().values()) {
      // Checks if some id is equal the given id.
      if (!memberModel.id().equals(playerId)) {
        continue;
      }
      return true;
    }
    return false;
  }
  
  /**
   * Checks if the clan id given is already used by another clan.
   *
   * @param clanId ID of the clan.
   *
   * @return Will return true if there aren't another clan with the same id. Else, will return false.
   */
  public boolean canBeCreated(final @NotNull String clanId) {
    return clans.containsKey(clanId);
  }
  
  /**
   * Tries to return the in-memory reference for the id given, if the reference doesn't exist will return null.
   *
   * @param clanId ID of the clan.
   *
   * @return A {@link ClanModel} reference or a null value.
   */
  public @Nullable ClanModel findOrNull(final @NotNull String clanId) {
    return clans.get(clanId);
  }
  
  /**
   * Tries to return the clan model for the player id specified, if the reference doesn't exist will return null.
   *
   * @param id ID of the player.
   *
   * @return A {@link ClanModel} reference or a null value.
   */
  public @Nullable ClanModel findById(final @NotNull String id) {
    for (final var clanModel : clans.values()) {
      // Checks if the id is from a clan member.
      if (!clanModel.isMember(id)) {
        continue;
      }
      return clanModel;
    }
    return null;
  }
  
  /**
   * Deletes the clan specified and remove their reference from memory.
   *
   * @param clanId ID of the clan to delete.
   *
   * @return If the clan were deleted correctly, will return a {@link ClanManager.Result#SHOULD_CLAN_DELETED} enum type for the operation.
   * <p>Else will return a {@link ClanManager.Result#NO_CLAN_REMOVE} enum type.
   */
  public @NotNull Result delete(final @NotNull String clanId) {
    return clans.remove(clanId) != null
      ? Result.SHOULD_CLAN_DELETED
      : Result.NO_CLAN_REMOVE;
  }
  
  /**
   * Mark to the player given as leader of the clan given, then, return a {@link Result} enum type.
   *
   * @param playerId ID of the new leader.
   * @param clanModel Clan that will have leader change.
   *
   * @return If the change were applied this method will return a {@link ClanManager.Result#SHOULD_LEADER_CHANGED} enum type.
   * <p>If the player id given is the same to the old leader, will return a {@link ClanManager.Result#SAME_CLAN_LEADER} enum type.
   */
  public @NotNull Result changeLeader(final @NotNull String playerId, final @NotNull ClanModel clanModel) {
    final var oldLeader = clanModel.leader();
    // Checks if the id given is the same of the old leader.
    if (playerId.equals(oldLeader)) {
      return Result.SAME_CLAN_LEADER;
    }
    clanModel.setLeader(playerId);
    return Result.SHOULD_LEADER_CHANGED;
  }
  
  /**
   * Checks if the player given is spying.
   *
   * @param playerId ID of the player.
   *
   * @return This method will return true if the player yes it is spying, else will return false.
   */
  public boolean isSpying(final @NotNull String playerId) {
    return false;
  }
  
  /**
   * Removes all the clan models that are in cache memory.
   */
  public void clear() {
    clans.clear();
  }

  public enum Result {
    SUCCESS,
    ALREADY_CLAN_CREATED,
    SHOULD_CLAN_DELETED,
    NO_CLAN_REMOVE,
    SAME_CLAN_LEADER,
    SHOULD_LEADER_CHANGED
  }
}
