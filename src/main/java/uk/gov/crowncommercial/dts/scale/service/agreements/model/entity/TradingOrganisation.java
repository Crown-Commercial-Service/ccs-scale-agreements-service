package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;

/**
 * Lot.
 */
@Entity
@Immutable
@Table(name = "trading_organisations")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Cacheable
public class TradingOrganisation {

  @Id
  @Column(name = "trading_organisation_id")
  Integer id;

  @Column(name = "trading_organisation_name")
  String tradingOrganisationName;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  Organisation organisation;
}