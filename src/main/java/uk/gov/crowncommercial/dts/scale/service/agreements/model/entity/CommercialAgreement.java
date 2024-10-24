package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidAgreementException;

/**
 * Commercial Agreement.
 */
@Entity
@Table(name = "commercial_agreements")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = "benefits")
public class CommercialAgreement {

  @Id
  @Column(name = "commercial_agreement_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @Column(name = "commercial_agreement_number")
  String number;

  @Column(name = "commercial_agreement_name")
  String name;

  @Column(name = "commercial_agreement_owner")
  String owner;

  @Column(name = "commercial_agreement_description")
  String description;

  @Column(name = "start_date")
  LocalDate startDate;

  @Column(name = "end_date")
  LocalDate endDate;

  @Column(name = "agreement_url")
  String detailUrl;

  @OneToMany(mappedBy = "agreement")
  Set<Lot> lots;

  @OneToMany
  @JoinColumn(name = "commercial_agreement_id")
  Set<CommercialAgreementDocument> documents;

  @OneToMany
  @JoinColumn(name = "commercial_agreement_id")
  Set<CommercialAgreementUpdate> updates;

  @OneToMany
  @JoinColumn(name = "commercial_agreement_id")
  Set<CommercialAgreementOrgRole> organisationRoles;

  @JoinColumn(name = "commercial_agreement_id")
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  Set<CommercialAgreementBenefit> benefits = new LinkedHashSet<CommercialAgreementBenefit>();;

  @Column(name = "lot_required")
  Boolean preDefinedLotRequired;

  @Column(name = "lot_assessment_tool_id")
  Integer lotAssessmentTool;

  public Boolean getPreDefinedLotRequired() {
    return preDefinedLotRequired != null ? preDefinedLotRequired : Boolean.FALSE;
  }

  public CommercialAgreement map(Function<CommercialAgreement, CommercialAgreement> function) {
    return function.apply(this);
  }

  public CommercialAgreement(){}

  public CommercialAgreement(String number, String name, String owner, String description, LocalDate startDate, LocalDate endDate, String detailUrl, Boolean preDefinedLotRequired) {
    this.number = number;
    this.name = name;
    this.owner = owner;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    this.detailUrl = detailUrl;
    this.preDefinedLotRequired = preDefinedLotRequired;
  }

  public void isValid(){
    if (name == null || name.isEmpty()) {throw new InvalidAgreementException("name");}
    if (description == null || description.isEmpty()) {throw new InvalidAgreementException("description");}
    if (owner == null || owner.isEmpty()) {throw new InvalidAgreementException("ownerName");}
    if (startDate == null) {throw new InvalidAgreementException("startDate");}
    if (endDate == null) {throw new InvalidAgreementException("endDate");}
    if (detailUrl == null || detailUrl.isEmpty()) {throw new InvalidAgreementException("detailUrl");}
    if (preDefinedLotRequired == null) {throw new InvalidAgreementException("preDefinedLotRequired");}
  }

  public CommercialAgreementBenefit addBenefit(CommercialAgreementBenefit benefit){
    this.benefits.add(benefit);
    benefit.setAgreement(this);
    return benefit;
  }
}
