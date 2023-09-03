package net.velex.clans.api.enums;

public enum Result {
  /**
   * Indicates that the operation were completed successfully.
   */
  SUCCESS,
  /**
   * Indicates that there was an error to load the configuration models.
   */
  NO_CONFIG_LOAD,
  /**
   * Indicates that some or multiple dependencies weren't hooked correctly.
   */
  NO_HOOKED,
  /**
   * Indicates that there wasn't a correct handler for the operation.
   */
  NO_HANDLE,
  /**
   * Indicates that the clan received a leader change.
   */
  SHOULD_LEADER_CHANGE,
  /**
   * Indicates that the leader specified for the change is the same that the old leader.
   */
  SAME_CLAN_LEADER,
  /**
   * Indicates that the clan was deleted correctly.
   */
  SHOULD_CLAN_DELETE,
  /**
   * Indicates that the clan cannot be deleted fully.
   */
  NO_CLAN_DELETE,
  /**
   * Indicates that the clan specified is already created.
   */
  ALREADY_CLAN_CREATED,
  /**
   * Indicates that the member cannot be removed from clan.
   */
  NO_MEMBER_REMOVE,
  /**
   * Indicates that the member were removed from clan.
   */
  SHOULD_MEMBER_REMOVE,
  /**
   * Indicates that the member is already removed from clan.
   */
  ALREADY_MEMBER_REMOVE
}
