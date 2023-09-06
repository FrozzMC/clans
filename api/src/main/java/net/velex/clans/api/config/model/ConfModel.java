package net.velex.clans.api.config.model;

import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.annote.*;
import space.arim.dazzleconf.serialiser.URLValueSerialiser;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

@ConfSerialisers(URLValueSerialiser.class)
@ConfHeader("""
  Clans | By Qekly,
  Create, manage and duel with another clans to be the first on the top!
  """)
public interface ConfModel {
  @AnnotationBasedSorter.Order(10)
  @ConfKey("config.prefix")
  @ConfComments({
    "Prefix format for the plugin messages.",
    "Format can be used with the '<prefix>' placeholder."
  })
  @ConfDefault.DefaultString("&a[clans]")
  @NotNull String prefix();
  
  @AnnotationBasedSorter.Order(20)
  @ConfKey("config.clan.creation-interval")
  @ConfComments({
    "Interval time required for create a clan again.",
    "This value represent on seconds."
  })
  @ConfDefault.DefaultInteger(600)
  int clanCreationInterval();
  
  @AnnotationBasedSorter.Order(30)
  @ConfKey("config.clan.rename-interval")
  @ConfComments({
    "Interval time required for rename a clan again.",
    "This value represent on seconds."
  })
  @ConfDefault.DefaultInteger(90000)
  int clanRenameInterval();
  
  @AnnotationBasedSorter.Order(40)
  @ConfKey("config.clan.backup-interval")
  @ConfComments({
    "Interval time required for do another backup for the clan again.",
    "This value represent on seconds."
  })
  @ConfDefault.DefaultInteger(18000)
  int clanBackupInterval();
  
  @AnnotationBasedSorter.Order(50)
  @ConfKey("config.clan.top-update-interval")
  @ConfComments({
    "Interval time required for update the clans top.",
    "This value represent on seconds."
  })
  @ConfDefault.DefaultInteger(60)
  int clansTopUpdateInterval();
  
  @AnnotationBasedSorter.Order(60)
  @ConfKey("config.clan.clan-name-length")
  @ConfComments({
    "Maximum characters amount allowed for the clan name.",
    "If the clan name exceeds this value, the name will appear in-completed."
  })
  @ConfDefault.DefaultInteger(25)
  int clanNameLengthLimit();
  
  @AnnotationBasedSorter.Order(70)
  @ConfKey("config.clan.clan-tag-length")
  @ConfComments({
    "Maximum characters amount allowed for the clan tag.",
    "If the clan tag exceeds this value, the tag will appear in-completed."
  })
  @ConfDefault.DefaultInteger(5)
  int clanTagLengthLimit();
  
  @AnnotationBasedSorter.Order(80)
  @ConfKey("config.clan.player-tag-length")
  @ConfComments({
    "Maximum characters amount allowed for the player tag.",
    "If the player tag exceeds this value, the tag will appear in-completed."
  })
  @ConfDefault.DefaultInteger(10)
  int playerTagLengthLimit();
  
  @AnnotationBasedSorter.Order(90)
  @ConfKey("config.clan.clan-info-length")
  @ConfComments({
    "Maximum characters amount allowed for a the clan comment.",
    "If the clan comment exceeds this value, the comment will appear in-completed."
  })
  @ConfDefault.DefaultInteger(25)
  int clanInfoLengthLimit();
  
  @AnnotationBasedSorter.Order(100)
  @ConfKey("config.clan.chat-symbol")
  @ConfComments({
    "Symbol used for the clan chat messages.",
    "If the message starts with this symbol, the message will be sent to all clan members."
  })
  @ConfDefault.DefaultString("@")
  @NotNull String clanChatSymbol();
  
  @AnnotationBasedSorter.Order(110)
  @ConfKey("config.clan-chat-format")
  @ConfComments({
    "Format used for show the clan chat channel.",
    "You can use placeholders and colors here."
  })
  @ConfDefault.DefaultString("&8[&6Clan-Channel&8] &e<tag> &f<player> &8&l| &f<message>")
  @NotNull String clanChatFormat();
}
