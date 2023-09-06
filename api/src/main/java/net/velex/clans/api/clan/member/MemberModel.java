package net.velex.clans.api.clan.member;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class MemberModel {
  private final String id;
  private final String name;
  private final String tag;
  private final String comment;
  
  private short kills;
  private short deaths;
  private short points;
  private short wins;
  private short loses;
  
  public MemberModel(
    final @NotNull String id,
    final @NotNull String name,
    final @NotNull String tag,
    final @NotNull String comment,
    final short kills,
    final short deaths,
    final short points,
    final short wins,
    final short loses
  ) {
    this.id = Preconditions.checkNotNull(id, "User ID (UUID) cannot be null.");
    this.name = Preconditions.checkNotNull(name, "Member username cannot be null.");
    this.kills = kills;
    this.deaths = deaths;
    this.points = points;
    this.wins = wins;
    this.loses = loses;
    this.tag = Preconditions.checkNotNull(tag, "Member tag cannot be null.");
    this.comment = Preconditions.checkNotNull(comment, "Member comment cannot be null.");
  }
  
  public @NotNull String id() {
    return id;
  }
  
  public @NotNull String name() {
    return name;
  }
  
  public short kills() {
    return kills;
  }
  
  public short deaths() {
    return deaths;
  }
  
  public short points() {
    return points;
  }
  
  public short wins() {
    return wins;
  }
  
  public short loses() {
    return loses;
  }
  
  public @NotNull String tag() {
    return tag;
  }
  
  public @NotNull String comment() {
    return comment;
  }
}
