package net.velex.clans.api.config.model;

import org.jetbrains.annotations.NotNull;
import space.arim.dazzleconf.annote.*;
import space.arim.dazzleconf.serialiser.URLValueSerialiser;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.util.List;

@ConfSerialisers(URLValueSerialiser.class)
@ConfHeader("""
  Anti-Farming Config | By Qekly.
  Modify the options for the anti-farming plugin feature.
  """)
public interface AntiFarmConfModel {
  @AnnotationBasedSorter.Order(10)
  @ConfKey("type.same-address.use")
  @ConfComments("Do you want to enable the anti-farming detector for same IP address's?")
  @ConfDefault.DefaultBoolean(true)
  boolean useForSameAddress();

  @AnnotationBasedSorter.Order(20)
  @ConfKey("type.same-address.alert-message")
  @ConfComments("Alert message to send when a player kills their own alternate account.")
  @ConfDefault.DefaultString("&c[Anti-Farm] &8| &eYou just killed your alt, so your clan will lost 20 of respect.")
  @NotNull String sameAddressAlertMessage();

  @AnnotationBasedSorter.Order(30)
  @ConfKey("type.same-address.respect")
  @ConfComments("Respect amount to remove for the clan where the player is.")
  @ConfDefault.DefaultInteger(20)
  short penaltyRespectAmount();

  @AnnotationBasedSorter.Order(40)
  @ConfKey("type.same-address.commands")
  @ConfComments("Commands to execute when a player do kill-farming.")
  @ConfDefault.DefaultStrings("")
  @NotNull List<String> sameAddressPenaltyCommands();

  @AnnotationBasedSorter.Order(50)
  @ConfKey("type.record-kills.use")
  @ConfComments("Do you want to enable the anti-farming detector when a player kills another 'x' times?")
  @ConfDefault.DefaultBoolean(true)
  boolean useForRecordKills();

  @AnnotationBasedSorter.Order(60)
  @ConfKey("type.record-kills.alert-message")
  @ConfComments("Alert message to send when a player kills too many times to another player.")
  @ConfDefault.DefaultString("&c[Anti-Farm] &8| &eYou just killed too many times to that player, so your clan will lost 20 of respect.")
  @NotNull String recordKillsAlertMessage();

  @AnnotationBasedSorter.Order(70)
  @ConfKey("type.record-kills.reset-interval")
  @ConfComments("""
    Time amount until the record were resetted.
    This value is represented on seconds.
    """)
  @ConfDefault.DefaultInteger(60)
  short recordKillsResetInterval();

  @AnnotationBasedSorter.Order(80)
  @ConfKey("type.record-kills.maximum-kills")
  @ConfComments("Maximum kills amount allowed for the player, after this amount will be considered as kill-farming.")
  @ConfDefault.DefaultInteger(5)
  short recordKillsLimit();

  @AnnotationBasedSorter.Order(90)
  @ConfKey("type.record-kills.respect")
  @ConfComments("Respect amount to remove for the clan where the player is.")
  @ConfDefault.DefaultInteger(20)
  short recordKillsRespectAmount();

  @AnnotationBasedSorter.Order(100)
  @ConfKey("type.record-kills.commands")
  @ConfComments("Commands to execute when a player do kill-farming.")
  @ConfDefault.DefaultStrings("")
  @NotNull List<String> recordKillsPenaltyCommands();
}
