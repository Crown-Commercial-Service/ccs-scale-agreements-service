package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Lot Summary.
 */
@Data
public class LotSummary implements Serializable {

  /**
   * Lot number e.g. "1" for Lot 1.
   */
  private String number;

  /**
   * Lot name e.g. "Finance".
   */
  private String name;
}
