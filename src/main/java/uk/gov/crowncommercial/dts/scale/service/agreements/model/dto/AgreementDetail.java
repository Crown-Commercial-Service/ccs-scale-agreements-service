package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.Data;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.*;

/**
 * Agreement Detail.
 */
@Data
public class AgreementDetail implements Serializable {

  /**
   * Commercial Agreement Number e.g. "RM1045".
   */
  private String number;

  /**
   * Commercial Agreement Name e.g. "Technology Products 2".
   */
  private String name;

  /**
   * Short textual description of the commercial agreement.
   */
  private String description;

  /**
   * Commercial Agreement Owner e.g. "CCS".
   */
  private String ownerName;

  /**
   * Effective start date of Commercial Agreement.
   */
  private LocalDate startDate;

  /**
   * Effective end date of Commercial Agreement.
   */
  private LocalDate endDate;

  /**
   * Effective start date of Commercial Agreement.
   */
  private String detailUrl;

  /**
   * A party (organization).
   */
  private Organization owner;

  /**
   * Contacts.
   */
  private Collection<Contact> contacts;

  /**
   * Short description of the benefit.
   */
  private Collection<String> benefits;

  /**
   * Associated lots.
   */
  private Collection<LotSummary> lots;

  /**
   * Assessment ID provided when a new assessment is created.
   */
  private Integer lotAssessmentTool;

  /**
   * Defines if a project can be set up without defining a lot. The lot is defined as an assessment provided by the agreement.
   */
  private Boolean preDefinedLotRequired;

  /**
   * TPP- The regulation that this agreement is under
   */
  private Regulation regulation;

  /**
   * TPP- The agreement type of this agreement
   */
  private AgreementType agreementType;

  public AgreementDetail() {
  }

  public AgreementDetail(String name, String description, String ownerName, LocalDate startDate, LocalDate endDate, String url, boolean preDefinedLotRequired) {
    this.name = name;
    this.description = description;
    this.ownerName = ownerName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.detailUrl = url;
    this.preDefinedLotRequired = preDefinedLotRequired;
  }

  public void setRegulation(String regulationInput) {
    try {
      if (regulationInput != null) {
        this.regulation = Regulation.valueOf(regulationInput.toUpperCase());
      }
    } catch (IllegalArgumentException e) {
      throw new InvalidRegulationException(regulationInput);
    }
  }

  public void setAgreementType(String agreementTypeInput) {

    if (agreementTypeInput != null){
      List<AgreementType> allowList = Arrays.asList();
      boolean error = false;
      AgreementType userInputedEnum = AgreementType.getAgreementTypeFromName(agreementTypeInput);

      if (userInputedEnum == null) {throw new InvalidAgreementTypeException(agreementTypeInput);};

      if (this.regulation != null) {
        switch (this.regulation) {
          case PA2023:
            allowList = Arrays.asList(AgreementType.DYNAMIC_MARKET, AgreementType.OPEN_FRAMEWORK, AgreementType.CLOSED_FRAMEWORK);
            error = !allowList.contains(userInputedEnum);
            break;
          case PCR2015:
            allowList = Arrays.asList(AgreementType.DYNAMIC_PURCHASING_SYSTEM, AgreementType.PCR15_FRAMEWORK);
            error = !allowList.contains(userInputedEnum);
            break;
          case PCR2006:
            allowList = Arrays.asList(AgreementType.PCR06_FRAMEWORK);
            error = !allowList.contains(userInputedEnum);
            break;
        }

        if (error) {throw new InvalidAgreementTypeException(AgreementType.getStringFormatForAgreementTypes(allowList), regulation);}

        this.agreementType = userInputedEnum;
      }
    }
  }

}
