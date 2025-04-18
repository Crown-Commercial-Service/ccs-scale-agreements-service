package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidLotException;

/**
 * Lot.
 */
@Entity
@EqualsAndHashCode(exclude = {"agreement", "procurementEventTypes"})
@Table(name = "lots")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@jakarta.persistence.Cacheable
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
  Set<LotProcurementEventType> procurementEventTypes;

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