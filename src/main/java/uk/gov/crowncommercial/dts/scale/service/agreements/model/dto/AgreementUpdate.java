package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.time.LocalDate;
import lombok.Data;

/**
 * Agreement Update.
 */
@Data
public class AgreementUpdate {

  /**
   * Date that the update was added.
   */
  private LocalDate date;

  /**
   * Link to further information regarding the update.
   */
  private String linkUrl;

  /**
   * Actual update text.
   */
  private String text;

}
