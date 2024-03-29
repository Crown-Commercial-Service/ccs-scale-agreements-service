package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Data required from the relevant transaction to be able to evaluate a rule.
 */
@Data
public class TransactionData implements Serializable {

  /**
   * Name of the variable.
   */
  private String name;

  /**
   * Path or other location of the data which can be evaluated by the application.
   */
  private String location;
}
