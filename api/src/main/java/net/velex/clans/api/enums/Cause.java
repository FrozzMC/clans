package net.velex.clans.api.enums;

public enum Cause {
  /**
   * Indicates that the clan was created by a player.
   */
  CREATION_BY_PLAYER,
  /**
   * Indicates that the clan was created by the plugin.
   */
  CREATION_BY_API,
  /**
   * Indicates that the clan was deleted by the plugin,
   */
  DELETE_BY_API,
  /**
   * Indicates that the clan was deleted by an administrator.
   */
  DELETE_BY_ADMIN,
  /**
   * Indicates that the clan was deleted by the leader.
   */
  DELETE_BY_LEADER
}
