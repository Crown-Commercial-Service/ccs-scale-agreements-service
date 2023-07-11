package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Address (e.g. of an Organization)
 */
@Data
public class Address implements Serializable {

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

  /**
   * The country code.
   */
  private String countryCode;

}
