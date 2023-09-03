package net.velex.clans.api.clan.member;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class MemberModel {
  private final String id;
  private final String name;
  private final String tag;
  private final String comment;
  
  private int kills;
  private int deaths;
  private short points;
  private int wins;
  private int loses;
  
  public MemberModel(
    final @NotNull String id,
    final @NotNull String name,
    final @NotNull String tag,
    final @NotNull String comment,
    final int kills,
    final int deaths,
    final short points,
    final int wins,
    final int loses
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
  
  public int kills() {
    return kills;
  }
  
  public int deaths() {
    return deaths;
  }
  
  public short points() {
    return points;
  }
  
  public int wins() {
    return wins;
  }
  
  public int loses() {
    return loses;
  }
  
  public @NotNull String tag() {
    return tag;
  }
  
  public @NotNull String comment() {
    return comment;
  }
}
