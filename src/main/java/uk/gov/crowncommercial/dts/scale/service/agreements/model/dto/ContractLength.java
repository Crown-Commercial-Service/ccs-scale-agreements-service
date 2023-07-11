package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Contract Length.
 */
@Data
@AllArgsConstructor
public class ContractLength implements Serializable {

  /**
   * The unit of the length value.
   */
  private String unit;

  /**
   * The number of units.
   */
  private Short length;
}
