package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

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
  @ManyToMany
  @JoinTable(name = "lot_sectors", joinColumns = @JoinColumn(name = "lot_id"),
      inverseJoinColumns = @JoinColumn(name = "sector_code"))
  Set<Sector> sectors;

  @ToString.Exclude
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRouteToMarket> routesToMarket;

  @ToString.Exclude
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRule> rules;

  @ToString.Exclude
  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRelatedLot> relatedAgreementLots;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotOrganisationRole> organisationRoles;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotProcurementEventType> procurementEventTypes;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotProcurementQuestionTemplate> procurementQuestionTemplates;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name ="lot_id")
  Set<TemplateGroup> templateGroups;
}
