package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotRoutesToMarket")
public class LotRouteToMarket {

  @EmbeddedId
  LotRouteToMarketKey key;

  @MapsId("routeToMarketName")
  @ManyToOne
  @JoinColumn(name = "route_to_market_name")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "routeToMarket")
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
