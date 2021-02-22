package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

/**
 *
 */
@Data
public class Address {

  /**
   * The street address. For example, 1600 Amphitheatre Pkwy.
   */
  private String streetAddress;

  /**
   * The locality. For example, Mountain View.
   */
  private String locality;

  /**
   * The region. For example, CA.
   */
  private String region;

  /**
   * The postal code. For example, 94043.
   */
  private String postalCode;

  /**
   * The country name. For example, United States.
   */
  private String countryName;

}
