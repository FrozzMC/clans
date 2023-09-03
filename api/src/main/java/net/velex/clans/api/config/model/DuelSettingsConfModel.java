package net.velex.clans.api.config.model;

import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.annote.*;
import space.arim.dazzleconf.serialiser.URLValueSerialiser;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

@ConfSerialisers(URLValueSerialiser.class)
@ConfHeader("""
  Duel-Settings Config | By Qekly
  Sets the settings for the duel requests.
  """)
public interface DuelSettingsConfModel {
  @AnnotationBasedSorter.Order(10)
  @ConfKey("request-send-time")
  @ConfComments("""
    Time until the request will expire.
    This value is on seconds.
    """)
  @ConfDefault.DefaultInteger((byte) 10)
  byte requestSendMaxTime();
  
  @AnnotationBasedSorter.Order(20)
  @ConfKey("request-receive-time")
  @ConfComments("""
    Time until the accepted request will expire.
    This value is on seconds.
    """)
  @ConfDefault.DefaultInteger((byte) 30)
  byte requestReceiveMaxTime();
  
  @AnnotationBasedSorter.Order(30)
  @ConfKey("duel-previous-start-time")
  @ConfComments("""
    Time before the duel will start.
    This value is on seconds.
    """)
  @ConfDefault.DefaultInteger((byte) 5)
  byte duelPreStartTime();
  
  @AnnotationBasedSorter.Order(40)
  @ConfKey("duel-time")
  @ConfComments("""
    Maximum duration for the duel.
    This value is on seconds.
    """)
  @ConfDefault.DefaultInteger((short) 600)
  short duelMaxTime();
  
  @AnnotationBasedSorter.Order(40)
  @ConfKey("duel-previous-end-time")
  @ConfComments("""
    Time until all the players in the duel will sent back and duel end.
    This value is on seconds.
    """)
  @ConfDefault.DefaultInteger((byte) 3)
  byte duelPreEndTime();
  
  @AnnotationBasedSorter.Order(50)
  @ConfKey("max-team-size")
  @ConfComments("Maximum players amount for each team in a duel.")
  @ConfDefault.DefaultInteger((byte) 8)
  byte duelMaxTeamSize();
  
  @AnnotationBasedSorter.Order(60)
  @ConfKey("min-team-size")
  @ConfComments("Minimum players amount for each team in a duel.")
  @ConfDefault.DefaultInteger((byte) 2)
  byte duelMinTeamSize();
  
  @AnnotationBasedSorter.Order(70)
  @ConfKey("notify-request-with-titles")
  @ConfComments("Do you want to notify future duel request using titles?")
  @ConfDefault.DefaultBoolean(true)
  boolean notifyRequestWithTitles();
  
  @AnnotationBasedSorter.Order(80)
  @ConfKey("messages.title-request")
  @ConfComments("Request message on the title.")
  @ConfDefault.DefaultString("&a&lDUEL REQUEST RECEIVED")
  @NotNull String titleDuelRequest();
  
  @AnnotationBasedSorter.Order(90)
  @ConfKey("messages.subtitle-request")
  @ConfComments("Request message on the subtitle.")
  @ConfDefault.DefaultString("&fTo accept the duel, type: &7/clan accept")
  @NotNull String subtitleDuelRequest();
  
  @AnnotationBasedSorter.Order(100)
  @ConfKey("messages.on-request")
  @ConfComments("Message when a duel request is received.")
  @ConfDefault.DefaultStrings({
    "&f-------------------------------",
    "    &b&lClan &f&lDuels:",
    "&f%clan%&f has challenged",
    "'&fyour clan to a duel.",
    "",
    "&b&lDuel Settings:",
    "&7- &eArena : &7%arena%",
    "&7- &aTeam size : &7%teamsize%",
    "&7- &6Kit : &7%kit%",
    "&7- &cKeep inventory : &7%keepinv%",
    "",
    "&f-------------------------------",
    "      &f&lClick Here to accept.<cc>/clan accept<cc>",
    "&f-------------------------------"
  })
  @NotNull String requestMotd();
}
