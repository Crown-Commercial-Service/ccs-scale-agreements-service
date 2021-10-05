package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;
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

  @ManyToMany
  @JoinTable(name = "lot_sectors", joinColumns = @JoinColumn(name = "lot_id"),
      inverseJoinColumns = @JoinColumn(name = "sector_code"))
  Set<Sector> sectors;

  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRouteToMarket> routesToMarket;

  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRule> rules;

  @OneToMany
  @JoinColumn(name = "lot_id")
  Set<LotRelatedLot> relatedAgreementLots;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotOrganisationRole> organisationRoles;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id")
  Set<LotProcurementEventType> procurementEventTypes;
}
