package net.velex.clans.api.model;

import net.velex.clans.api.enums.Result;
import org.jetbrains.annotations.NotNull;

public interface HookManagerModel {
  @NotNull Result hook();
}
