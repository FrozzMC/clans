package net.velex.clans.api.clan;

import com.google.common.base.Preconditions;
import net.velex.clans.api.clan.base.BaseModel;
import net.velex.clans.api.clan.member.MemberModel;
import net.velex.clans.api.enums.Result;
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
  
  public @NotNull String id() {
    return id;
  }
  
  public @NotNull String leader() {
    return leader;
  }
  
  public void setLeader(final @NotNull String newLeaderId) {
    leader = Preconditions.checkNotNull(newLeaderId, "The new leader ID cannot be null.");
  }
  
  public BaseModel @NotNull[] bases() {
    return bases;
  }
  
  public short wins() {
    return wins;
  }
  
  public short loses() {
    return loses;
  }
  
  public short respect() {
    return respect;
  }
  
  public @NotNull Map<String, MemberModel> members() {
    return members;
  }
  
  public void addMember(final @NotNull Player player) {
    final var id = player.getUniqueId().toString();
    members.put(id, new MemberModel(
      id,
      player.getName(),
      "", "",
      0, 0, (short) 0, 0, 0));
  }
  
  public @NotNull Result removeMember(final @NotNull String id) {
    // Checks if the member is already removed.
    if (!members.containsKey(id)) {
      return Result.ALREADY_MEMBER_REMOVE;
    }
    final var memberModel = members.remove(id);
    // Checks if the reference exists yet.
    if (memberModel == null) {
      return Result.NO_MEMBER_REMOVE;
    }
    return Result.SHOULD_MEMBER_REMOVE;
  }
  
  public void incrementWins(final short amount) {
    wins += amount;
  }
  
  public void incrementLoses(final short amount) {
    loses += amount;
  }
  
  public void incrementRespect(final short amount) {
    respect += amount;
  }
  
  public void decrementWins(final short amount) {
    wins -= amount;
  }
  
  public void decrementLoses(final short amount) {
    loses -= amount;
  }
  
  public void decrementRespect(final short amount) {
    respect -= amount;
  }
}
