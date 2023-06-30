package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * Name Value Type.
 */
@Data
public class NameValueType implements Serializable {

  /**
   * The name of the 'other' bound to be used as map key.
   */
  private String name;

  /**
   * The data type of the 'other' lotBound value
   */
  private DataType dataType;

  private String valueText;
  private Integer valueInteger;
  private BigDecimal valueNumber;
  private LocalDate valueDate;

}
