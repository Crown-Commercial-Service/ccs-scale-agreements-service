package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

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
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "trading_organisations") //Provide cache strategy.
public class TradingOrganisation {

  @Id
  @Column(name = "trading_organisation_id")
  Integer id;

  @Column(name = "trading_organisation_name")
  String tradingOrganisationName;

  @ManyToOne
  @JoinColumn(name = "organisation_id")
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "organisation")
  Organisation organisation;
}
