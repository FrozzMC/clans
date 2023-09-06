package net.velex.clans.enums;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public enum ClanNode {
  UPGRADE_HOME ("maxhomes", null, null, 1),
  UPGRADE_MEMBERS ("maxmembers", null, null, 2),
  UPGRADE_VAULTS ("maxvaults", null, null, 1),
  UPGRADE_DUELS ("maxduels", null, null, 2),
  UPGRADE_ALLIES ("maxallies", null, null, 1),
  
  DUELS_WINS ("wonduels", null, null, 0),
  DUELS_LOSES ("lostduels", null, null, 0),
  
  CLAN_NAME ("name", "", null, -1),
  CLAN_NAME_RAW ("nameraw", "", null, -1),
  CLAN_LEADER ("leader", "", null, -1),
  CLAN_MEMBERS ("members", null, new ArrayList<>(), -1),
  CLAN_ALLIES ("ally", null, new ArrayList<>(), -1),
  CLAN_TRUCES ("truce", null, new ArrayList<>(), -1),
  CLAN_BALANCE ("balance", null, null, 0),
  CLAN_VAULT ("vaults", "", null, -1),
  CLAN_BASES ("base", "", null, -1),
  CLAN_TAG ("tag", "", null, -1),
  
  RESPECT_ADITION ("respectadd", "", List.of(), 0),
  RESPECT_SUBTRACTION ("respectsub", "", List.of(), 0),
  
  RENAME_TIME ("renametime", "", List.of(), 0);
  
  private final String node;
  private final String defaultStringValue;
  private final List<String> defaultListValue;
  private final short defaultNumberValue;
  
  ClanNode(
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
