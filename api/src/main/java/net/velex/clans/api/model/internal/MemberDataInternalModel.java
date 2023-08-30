package net.velex.clans.api.model.internal;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class MemberDataInternalModel {
  private final String id;
  private final String name;
  private final short kills;
  private final short deaths;
  private final short points;
  private final int wins;
  private final int loses;
  private final String tag;
  private final String comment;
  
  public MemberDataInternalModel(
    final @NotNull String id,
    final @NotNull String name,
    final short kills,
    final short deaths,
    final short points,
    final int wins,
    final int loses,
    final @NotNull String tag,
    final @NotNull String comment
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
