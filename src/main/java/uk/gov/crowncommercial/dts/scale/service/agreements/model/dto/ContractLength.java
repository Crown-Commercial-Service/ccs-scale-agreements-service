package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Contract Length.
 */
@Data
@AllArgsConstructor
public class ContractLength {

  /**
   * The unit of the length value.
   */
  private String unit;

  /**
   * The number of units.
   */
  private Short length;
}
