package net.velex.clans.config.model;

import space.arim.dazzleconf.annote.*;
import space.arim.dazzleconf.serialiser.URLValueSerialiser;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

@ConfSerialisers(URLValueSerialiser.class)
@ConfHeader("""
  Clans | By Qekly
  
  List of sub-commands for the '/clan' command, specify which sub-commands you want to enable for this command.
  Every sub-command has own their permission, which can be got with the 'clans.%command%' permission.
  """)
public interface CommandsConfModel {
  @AnnotationBasedSorter.Order(10)
  @ConfKey("ally")
  @ConfComments("""
    Sub-command used for ally clan options.
    If is disabled, players can't use clan ally features.
    """)
  @ConfDefault.DefaultBoolean(true)
  boolean enableAllySubCommand();

  @AnnotationBasedSorter.Order(20)
  @ConfKey("base")
  @ConfComments("""
    Sub-command used for check base options.
    If is disabled, players can't check the bases and their options.
    """)
  @ConfDefault.DefaultBoolean(true)
  boolean enableBaseSubCommand();

  @AnnotationBasedSorter.Order(30)
  @ConfKey("chat")
  @ConfComments("""
    Sub-command used for manage the clan chat channel.
    If is disabled, players can't use the clan chat.
    """)
  @ConfDefault.DefaultBoolean(true)
  boolean enableChatSubCommand();

  @AnnotationBasedSorter.Order(40)
  @ConfKey("comments")
  @ConfComments("""
    Sub-command used for manage clan comments.
    If is disabled, players can't use clan comments.
    """)
  @ConfDefault.DefaultBoolean(true)
  boolean enableCommentSubCommand();
}
