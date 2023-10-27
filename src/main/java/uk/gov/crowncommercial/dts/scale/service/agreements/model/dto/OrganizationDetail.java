package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.function.Function;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Organization detail
 */
@Data
public class OrganizationDetail implements Serializable {

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

  public String getScale() {
    return scale;
  }

  public void setScale(String scale) {
    this.scale = scale;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getCompanyType() {
    return companyType;
  }

  public void setCompanyType(String companyType) {
    this.companyType = companyType;
  }

  public Boolean getVcse() {
    return isVcse;
  }

  public void setVcse(Boolean vcse) {
    isVcse = vcse;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getTradingName() {
    return tradingName;
  }

  public void setTradingName(String tradingName) {
    this.tradingName = tradingName;
  }

  public OrganizationDetail map(Function<OrganizationDetail, OrganizationDetail> function) {
    return function.apply(this);
  }
}
