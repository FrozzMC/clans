package net.velex.clans.api.model.internal;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BaseInternalModel {
  private final Map<String, Location> bases;
  
  public BaseInternalModel() {
    bases = new HashMap<>();
  }
  
  /**
   * Returns all the current bases into the cache memory.
   *
   * @return A {@link Map} reference with all the bases registered.
   */
  public @NotNull Map<String, Location> basesInMemory() {
    return bases;
  }
  
  /**
   * Store a new base in memory.
   *
   * @param name Name for the base.
   * @param location {@link Location} object for the base.
   */
  public void add(final @NotNull String name, final @NotNull Location location) {
    bases.putIfAbsent(name, location);
  }
  
  /**
   * Checks if the current base is saved into the file.
   *
   * @param name Name of the base.
   * @return A boolean value depending on the base is saved or not.
   */
  public boolean isSaved(final @NotNull String name) {
    return bases.get(name) == null;
  }
  
  /**
   * Removes the model for that name in memory.
   *
   * @param name Name for that base.
   */
  public void remove(final @NotNull String name) {
    bases.remove(name);
  }
  
  /**
   * Tries to find the {@link Location} object for that name into cache.
   *
   * @param name Name for the base to search.
   *
   * @return A {@link Location} object or null if the model isn't saved.
   */
  public @Nullable Location findOrNull(final @NotNull String name) {
    return bases.get(name);
  }
}
