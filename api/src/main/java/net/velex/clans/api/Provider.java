package net.velex.clans.api;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public class Provider {
  private static Clans clansApiInstance;
  
  /**
   * Returns the current {@link Clans} instance, but, if the instance isn't loaded yet<p>
   * will throw an exception {@link IllegalStateException} type.
   *
   * @return Must be a {@link Clans} object reference.
   */
  public static @NotNull Clans get() {
    // Checks if the instance hasn't an established reference.
    if (clansApiInstance == null) {
      throw new IllegalStateException("Could not get Clans object reference due that isn't loaded yet!");
    }
    return clansApiInstance;
  }
  
  /**
   * Tries to load a new reference for the current {@link Clans} instance, but, if the instance is loaded already<p>
   * will throw an exception {@link IllegalStateException} type. Avoid get the instance during the plugin load process.
   *
   * @param clansApiReference A reference of {@link Clans} object.
   */
  public static void load(final @NotNull Clans clansApiReference) {
    // Checks if the instance has already an established reference.
    if (clansApiInstance != null) {
      throw new IllegalArgumentException("Clans instance is already referenced.");
    }
    clansApiInstance = Preconditions.checkNotNull(clansApiReference, "Clans new reference cannot be null.");
  }
  
  /**
   * Just remove the current reference for the {@link Clans} instance, if the reference was deleted already, do nothing.
   */
  public static void unload() {
    // Checks if the reference was deleted already.
    if (clansApiInstance == null) {
      return;
    }
    clansApiInstance = null;
  }
}
