package net.velex.clans.api.clan;

import com.google.common.base.Preconditions;
import net.velex.clans.api.clan.base.BaseModel;
import net.velex.clans.api.clan.member.MemberModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ClanModel {
  private final String id;
  private final BaseModel[] bases;
  private final Map<String, MemberModel> members;
  
  private String leader;
  private short wins;
  private short loses;
  private short respect;
  
  public ClanModel(
    final @NotNull String id,
    final BaseModel @NotNull[] bases,
    final @NotNull Map<String, MemberModel> members,
    final @NotNull String leader
  ) {
    this.id = Preconditions.checkNotNull(id, "Clan ID cannot be null.");
    this.bases = Preconditions.checkNotNull(bases, "Array of current clan bases cannot be null.");
    this.members = Preconditions.checkNotNull(members, "Array of current clan members cannot be null.");
    this.leader = Preconditions.checkNotNull(leader, "Leader name cannot be null.");
  }
  
  /**
   * Returns the ID (or name) for this clan.
   *
   * @return The identifier for this clan.
   */
  public @NotNull String id() {
    return id;
  }
  
  /**
   * Returns the current leader of this clan.
   *
   * @return The clan leader.
   */
  public @NotNull String leader() {
    return leader;
  }
  
  public void setLeader(final @NotNull String newLeaderId) {
    leader = Preconditions.checkNotNull(newLeaderId, "The new leader ID cannot be null.");
  }
  
  /**
   * Checks if the id specified is from a clan member.
   *
   * @param id ID of the player.
   *
   * @return Wil return true if the player id specified is from a clan member. Else, will return false.
   */
  public boolean isMember(final @NotNull String id) {
    return members.containsKey(id);
  }
  
  /**
   * Returns the registered bases for this clan.
   *
   * @return The registered bases.
   */
  public BaseModel @NotNull[] bases() {
    return bases;
  }
  
  /**
   * Returns the wins amount of the clan.
   *
   * @return The wins amount.
   */
  public short wins() {
    return wins;
  }
  
  /**
   * Returns the loses amount of the clan.
   *
   * @return The loses amount.
   */
  public short loses() {
    return loses;
  }
  
  /**
   * Returns the respect amount of the clan.
   *
   * @return The respect amount.
   */
  public short respect() {
    return respect;
  }
  
  /**
   * Returns the current members of this clan reference.
   *
   * @return The members of the clan.
   */
  public @NotNull Map<String, MemberModel> members() {
    return members;
  }
  
  /**
   * Adds a new member to the current clan.
   *
   * @param player Reference for that member.
   */
  public void addMember(final @NotNull Player player) {
    final var id = player.getUniqueId().toString();
    members.put(id, new MemberModel(
      id,
      player.getName(),
      "", "",
      (short) 0,
      (short) 0,
      (short) 0,
      (short) 0,
      (short) 0));
  }
  
  /**
   * Removes to the member specified from the clan, and return a {@link ClanModel.Result} enum type.
   *
   * @param id ID of the member.
   *
   * @return If the member were already removed will return a {@link ClanModel.Result#ALREADY_MEMBER_REMOVED} enum type, if the model
   * <p>reference is null will return a {@link ClanModel.Result#NO_MEMBER_REMOVED} type. If the member were removed correctly
   * <p>will return the {@link ClanModel.Result#SUCCESS} type.
   */
  public @NotNull Result removeMember(final @NotNull String id) {
    // Checks if the member is already removed.
    if (!members.containsKey(id)) {
      return Result.ALREADY_MEMBER_REMOVED;
    }
    final var memberModel = members.remove(id);
    // Checks if the reference exists yet.
    if (memberModel == null) {
      return Result.NO_MEMBER_REMOVED;
    }
    return Result.SUCCESS;
  }
  
  /**
   * Increment the wins amount of the clan.
   *
   * @param amount The wins amount to add.
   */
  public void incrementWins(final short amount) {
    wins += amount;
  }
  
  /**
   * Increment the loses amount of the clan.
   *
   * @param amount The loses amount to add.
   */
  public void incrementLoses(final short amount) {
    loses += amount;
  }
  
  /**
   * Increment the respect amount of the clan.
   *
   * @param amount The respect amount to add.
   */
  public void incrementRespect(final short amount) {
    respect += amount;
  }
  
  /**
   * Decrement the wins amount of the clan.
   *
   * @param amount The wins amount to remove.
   */
  public void decrementWins(final short amount) {
    wins -= amount;
  }
  
  /**
   * Decrement the loses amount of the clan.
   *
   * @param amount The loses amount to remove.
   */
  public void decrementLoses(final short amount) {
    loses -= amount;
  }
  
  /**
   * Decrement the respect amount of the clan.
   *
   * @param amount The respect amount to remove.
   */
  public void decrementRespect(final short amount) {
    respect -= amount;
  }

  public enum Result {
    /**
     * Indicates that the operation were completed correctly and without failures.
     */
    SUCCESS,
    /**
     * Indicates that the member specified were already removed from this clan.
     */
    ALREADY_MEMBER_REMOVED,
    /**
     * Indicates that the member cannot be removed correctly.
     */
    NO_MEMBER_REMOVED
  }
}
