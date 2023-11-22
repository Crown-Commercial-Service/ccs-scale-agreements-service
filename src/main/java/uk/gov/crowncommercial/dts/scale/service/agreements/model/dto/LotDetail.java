package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import lombok.Data;

/**
 * Lot Detail.
 */
@Data
public class LotDetail implements Serializable {

  /**
   * Lot number e.g. "1" for Lot 1.
   */
  private String number;

  /**
   * Lot name e.g. "Finance".
   */
  private String name;

  /**
   * Effective start date of Lot.
   */
  private LocalDate startDate;

  /**
   * Effective end date of Lot.
   */
  private LocalDate endDate;

  /**
   * Short textual description of the Lot.
   */
  private String description;

  /**
   * Type of the lot.
   */
  private LotType type;

  /**
   * Routes to Market in lot.
   */
  private Collection<RouteToMarketDTO> routesToMarket;

  /**
   * A sector permitted to buy using the Agreement.
   */
  private Collection<String> sectors;

  /**
   * A simple reference to a related Agreement/Lot combination and relationship type.
   */
  private Collection<RelatedAgreementLot> relatedAgreementLots;

  /**
   * Buyer needs.
   */
  private Collection<BuyerNeed> buyerNeeds;

  /**
   * Rules.
   */
  private Collection<LotRuleDTO> rules;

  /**
   * Count of suppliers attached to the lot
   */
  private Integer supplierCount;

  public LotDetail() {
  }

  public LotDetail(String number, String name, LocalDate startDate, LocalDate endDate, String description, LotType type) {
    this.number = number;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
    this.type = type;
  }
}
