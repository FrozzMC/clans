package net.velex.clans.api.clan;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ClanTopManager {
  private final Map<Byte, ClanModel> clans;
  
  public ClanTopManager() {
    clans = new HashMap<>();
  }
  
  public @Nullable ClanModel findOrNull(final byte position) {
    return clans.get(position);
  }
  
  public @NotNull Map<Byte, ClanModel> sortedClans() {
    return clans;
  }
  
  public @NotNull Result setPosition(final @NotNull ClanModel clanModel, final byte position) {
    return Result.SUCCESS;
  }
  
  public void clear() {
    clans.clear();
  }
  
  public enum Result {
    SUCCESS,
    NO_POSITION_SET,
    ALREADY_POSITION_SET
  }
}
