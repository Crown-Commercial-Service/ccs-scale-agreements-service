package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

/**
 * Lot.
 */
@Entity
@Immutable
@EqualsAndHashCode(exclude = "agreement")
@Table(name = "lots")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@javax.persistence.Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lots") //Provide cache strategy.
public class Lot {

  @Id
  @Column(name = "lot_id")
  Integer id;

  @Column(name = "lot_number")
  String number;

  @Column(name = "lot_name")
  String name;

  @Column(name = "lot_description")
  String description;

  @Column(name = "lot_type")
  String lotType;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "commercial_agreement_id")
  CommercialAgreement agreement;

  @ToString.Exclude
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotSectors")
  @ManyToMany
  @JoinTable(name = "lot_sectors", joinColumns = @JoinColumn(name = "lot_id"),
      inverseJoinColumns = @JoinColumn(name = "sector_code"))
  Set<Sector> sectors;

  @ToString.Exclude
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotRoutesToMarket")
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRouteToMarket> routesToMarket;

  @ToString.Exclude
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotRules")
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRule> rules;

  @ToString.Exclude
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotRelatedAgreementLots")
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRelatedLot> relatedAgreementLots;

  // TODO: Refactor implementation
  @ToString.Exclude
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotOrganisationRoles")
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotOrganisationRole> organisationRoles;

  // TODO: Refactor implementation
  @ToString.Exclude
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotOrganisationRoles")
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id", insertable = false, updatable = false)
  @Where(clause="role_type_id = '2' and organisation_status = 'A'")
  Set<LotOrganisationRole> activeOrganisationRoles;

  @ToString.Exclude
  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotProcurementEventTypes")
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotProcurementEventType> procurementEventTypes;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotProcurementQuestionTemplates")
  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotProcurementQuestionTemplate> procurementQuestionTemplates;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name ="lot_id")
  Set<TemplateGroup> templateGroups;
}
