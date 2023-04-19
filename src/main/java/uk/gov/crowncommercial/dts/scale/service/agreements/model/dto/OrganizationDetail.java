package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Organization detail
 */
@Data
public class OrganizationDetail {

  /**
   * The size or scale of the organization.
   */
  private String scale;

  /**
   * Date of organisation creation
   */
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate creationDate;

  /**
   * Country code of incorporation
   */
  private String countryCode;

  /**
   * Type of company
   */
  private String companyType;

  /**
   * Is company voluntary, community and social enterprise
   */
  @JsonProperty("is_vcse")
  private Boolean isVcse;

  /**
   * Organisation Status
   */
  private String status;

  /**
   * Is organisation active?
   */
  private Boolean active;

  private String tradingName;

}
