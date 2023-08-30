package net.velex.clans.plugin.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IntUtils {
  public static float experience(final short playerLevel, final short playerExperience) {
    short experience = (short) Math.round(experienceAtLevel(playerLevel) * playerExperience);
    short currentLevel = playerLevel;
    while (currentLevel > 0) {
      currentLevel--;
      experience += experienceAtLevel(currentLevel);
    }
    // The current experience is lower than zero?
    if (experience < 0) {
      experience = 0;
    }
    return experience;
  }
  
  public static void setTotalExperience(final @NotNull Player player, final short experience) {
    player.setExp(0.0F);
    player.setLevel(0);
    player.setTotalExperience(0);
    
    short experienceAmount = experience;
    while (experienceAmount > 0) {
      short experienceToLevel = experienceAtLevel((short) player.getLevel());
      experienceAmount -= experienceToLevel;
      // Checks if the amount is higher or equal than zero.
      if (experienceAmount >= 0) {
        player.giveExp(experienceToLevel);
        continue;
      }
      experienceAmount += experienceToLevel;
      player.giveExp(experienceAmount);
      experienceAmount = 0;
    }
  }
  
  private static short experienceAtLevel(final short level) {
    // Checks if the level is lower or equal than 15.
    if (level <= 15) {
      return (short) ((2 * level) + 7);
    }
    // Checks if the level is lower or equal than 30.
    if (level <= 30) {
      return (short) ((5 * level) - 38);
    }
    return (short) ((9 * level) - 158);
  }
}
