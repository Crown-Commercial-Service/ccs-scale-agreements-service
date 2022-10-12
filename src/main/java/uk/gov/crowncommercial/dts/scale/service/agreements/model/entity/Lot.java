package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Data
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

  @ManyToOne
  @JoinColumn(name = "commercial_agreement_id")
  CommercialAgreement agreement;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotSectors")
  @ManyToMany
  @JoinTable(name = "lot_sectors", joinColumns = @JoinColumn(name = "lot_id"),
      inverseJoinColumns = @JoinColumn(name = "sector_code"))
  Set<Sector> sectors;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotRoutesToMarket")
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRouteToMarket> routesToMarket;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotRules")
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRule> rules;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotRelatedAgreementLots")
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRelatedLot> relatedAgreementLots;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotOrganisationRoles")
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotOrganisationRole> organisationRoles;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotProcurementEventTypes")
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotProcurementEventType> procurementEventTypes;

  @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "lotProcurementQuestionTemplates")
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotProcurementQuestionTemplate> procurementQuestionTemplates;
}
