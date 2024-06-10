package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Route To Market/Lot link table.
 */
@Entity
@Immutable
@Table(name = "lot_route_to_market")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LotRouteToMarket {

  @EmbeddedId
  LotRouteToMarketKey key;

  @MapsId("routeToMarketName")
  @ManyToOne
  @JoinColumn(name = "route_to_market_name")
  RouteToMarket routeToMarket;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;

  @Column(name = "location")
  String location;

  @Column(name = "buying_method_url")
  String buyingMethodUrl;

  @Column(name = "lot_minimum_value")
  BigDecimal minimumValue;

  @Column(name = "lot_maximum_value")
  BigDecimal maximumValue;

  @Column(name = "lot_contract_length_uom")
  String contractLengthUnitOfMeasure;

  @Column(name = "lot_contract_length_minimum_value")
  Short contractLengthMinimumValue;

  @Column(name = "lot_contract_length_maximum_value")
  Short contractLengthMaximumValue;
}
