package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidLotException;

/**
 * Lot.
 */
@Entity
@EqualsAndHashCode(exclude = "agreement")
@Table(name = "lots")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@javax.persistence.Cacheable
public class Lot {

  @Id
  @Column(name = "lot_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  @Formula("(SELECT COUNT(*) FROM lot_organisation_roles r WHERE r.lot_id = lot_id and r.role_type_id = '2' and r.organisation_status = 'A')")
  Integer supplierCount;

  public Lot map(Function<Lot, Lot> function) {
    return function.apply(this);
  }

  public void isValid(){
    if (number == null || number.isEmpty()) {throw new InvalidLotException("number", number);}
    if (name == null || name.isEmpty()) {throw new InvalidLotException("name", number);}
    if (description == null || description.isEmpty()) {throw new InvalidLotException("description", number);}
    if (lotType == null || lotType.isEmpty()) {throw new InvalidLotException("lotType", number);}
    if (startDate == null) {throw new InvalidLotException("startDate", number);}
    if (endDate == null) {throw new InvalidLotException("endDate", number);}
    if (agreement == null) {throw new InvalidLotException("agreement", number);}
  }

  public Lot() {
  }

  public Lot(String number, String name, String description, String lotType, LocalDate startDate, LocalDate endDate, CommercialAgreement agreement) {
    this.number = number;
    this.name = name;
    this.description = description;
    this.lotType = lotType;
    this.startDate = startDate;
    this.endDate = endDate;
    this.agreement = agreement;
  }
}
