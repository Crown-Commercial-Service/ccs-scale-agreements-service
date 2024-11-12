package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Buyer Need.
 */
@Data
public class BuyerNeed implements Serializable {

  /**
   * Id of the need (to allow alignment with CaT).
   */
  private Integer id;

  /**
   * Name of Buyer Need.
   */
  private String name;

  /**
   * Text to be displayed for Buyer need.
   */
  private String text;
}
