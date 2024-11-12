package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Agreement Summary.
 */
@Data
public class AgreementSummary implements Serializable {

  /**
   * Commercial Agreement Number, e.g. "RM1045".
   */
  private String number;

  /**
   * Commercial Agreement Name e.g. "Technology Products 2".
   */
  private String name;
}
