package net.velex.clans.api;

import net.velex.clans.api.model.*;
import org.jetbrains.annotations.NotNull;

public interface Clans {
  @NotNull ConfManagerModel confManagerModel();
  
  @NotNull ClanManagerModel clanManagerModel();
  
  @NotNull ClanTopManagerModel clanTopManagerModel();
  
  @NotNull ClanChatHandlerModel clanChatHandlerModel();
  
  @NotNull HookManagerModel hookManagerModel();

  @NotNull AntiFarmManagerModel antiFarmManagerModel();
}
