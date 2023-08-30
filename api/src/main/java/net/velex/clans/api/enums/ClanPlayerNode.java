package net.velex.clans.api.enums;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public enum ClanPlayerNode {
  KILLS ("kills", null, null, 0),
  DEATHS ("deaths", null, null, 0),
  POINTS ("points", null, null, 0),
  DUELS_PLAYED ("duels", null, null, 0),
  DUELS_WON ("duelswon", null, null, 0),
  TAG ("tag", "&aMember", null, -1),
  INFO ("info","Member of a clan", null, -1),
  PERMS ("perms", null, new ArrayList<>(), -1);
  
  private final String node;
  private final String defaultStringValue;
  private final List<String> defaultListValue;
  private final short defaultNumberValue;
  
  ClanPlayerNode(
    final @NotNull String node,
    final @Nullable String defaultStringValue,
    final @Nullable List<String> defaultListValue,
    final short defaultNumberValue
  ) {
    this.node = node;
    this.defaultStringValue = defaultStringValue;
    this.defaultListValue = defaultListValue;
    this.defaultNumberValue = defaultNumberValue;
  }
  
  public @NotNull String node() {
    return node;
  }
  
  public @Nullable String defaultStringValue() {
    return defaultStringValue;
  }
  
  public @Nullable List<String> defaultListValue() {
    return defaultListValue;
  }
  
  public short defaultNumberValue() {
    return defaultNumberValue;
  }
}
