package uk.gov.crowncommercial.dts.scale.service.agreements.config;

import lombok.experimental.UtilityClass;

/**
 * Constants for use within the application
 */
@UtilityClass
public class Constants {
  public static final String OCDS_ROLE_FRAMEWORK_OWNER = "frameworkOwner";

  // Prefix for DUNs numbers replacement
  public static final String DUNS_PREFIX = "US-DUNS-";

  // Error messages
    public static final String ERR_MSG_INVALID_REQUEST_EVENT_TYPE_MGMT = "Invalid request - required data missing for event type management";
}