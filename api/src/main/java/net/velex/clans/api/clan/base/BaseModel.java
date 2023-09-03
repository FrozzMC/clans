package net.velex.clans.api.clan.base;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BaseModel {
  private final Map<String, Location> basesLocations;
  
  public BaseModel() {
    basesLocations = new HashMap<>();
  }
  
  /**
   * Returns all the current bases into the cache memory.
   *
   * @return A {@link Map} reference with all the bases registered.
   */
  public @NotNull Map<String, Location> bases() {
    return basesLocations;
  }
  
  /**
   * Store a new base in memory.
   *
   * @param name Name for the base.
   * @param location {@link Location} object for the base.
   */
  public void add(final @NotNull String name, final @NotNull Location location) {
    basesLocations.putIfAbsent(name, location);
  }
  
  /**
   * Checks if the current base is saved into the file.
   *
   * @param name Name of the base.
   * @return A boolean value depending on the base is saved or not.
   */
  public boolean isSaved(final @NotNull String name) {
    return basesLocations.get(name) == null;
  }
  
  /**
   * Removes the model for that name in memory.
   *
   * @param name Name for that base.
   */
  public void remove(final @NotNull String name) {
    basesLocations.remove(name);
  }
  
  /**
   * Tries to find the {@link Location} object for that name into cache.
   *
   * @param name Name for the base to search.
   *
   * @return A {@link Location} object or null if the model isn't saved.
   */
  public @Nullable Location findOrNull(final @NotNull String name) {
    return basesLocations.get(name);
  }
}
