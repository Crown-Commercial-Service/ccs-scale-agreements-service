package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Immutable
@Table(name = "route_to_market")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteToMarket {

  @Id
  @Column(name = "route_to_market_name")
  String name;

  @Column(name = "route_to_market_description")
  String description;
}
