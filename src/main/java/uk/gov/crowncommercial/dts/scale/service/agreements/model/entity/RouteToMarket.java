package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Route To Market.
 */
@Entity
@Immutable
@Table(name = "route_to_market")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "routeToMarket")
public class RouteToMarket {

  @Id
  @Column(name = "route_to_market_name")
  String name;

  @Column(name = "route_to_market_description")
  String description;
}
