package net.velex.clans.api.model;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface AntiFarmManagerModel {
  void start();

  void stop();

  boolean isFarming(final @NotNull Player alpha, final @NotNull Player bravoTarget);
}
