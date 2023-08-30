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
  NO_HANDLE
}
