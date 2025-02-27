package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.ProcurementQuestionTemplateNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotEventTypeUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.*;

/**
 * Agreement Service.
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementService {

  private final CommercialAgreementRepo commercialAgreementRepo;
  private final LotRepo lotRepo;
  private final ProcurementEventTypeRepo procurementEventTypeRepo;
  private final LotProcurementEventTypeRepo lotProcurementEventTypeRepo;
  private final ProcurementQuestionTemplateRepo procurementQuestionTemplateRepo;
  private final SimpleLotRepo simpleLotRepo;
  private final CommercialAgreementBenefitService commercialAgreementBenefitService;

  /**
   * Get all agreements.
   *
   * @return list of agreements
   */
  public List<CommercialAgreement> getAgreements() {
    log.debug("getAgreements");
    return commercialAgreementRepo.findAll();
  }

  /**
   * Find a specific agreement by number.
   *
   * @param caNumber Commercial Agreement number
   * @return CommercialAgreement
   */
  public CommercialAgreement findAgreementByNumber(final String caNumber) {
    log.debug("findAgreementByNumber: {}", caNumber);

    return commercialAgreementRepo.findByNumber(caNumber).orElseThrow(() -> new AgreementNotFoundException(caNumber) );
  }

    /**
     * Create or update the agreement that is passed in
     *
     * @param model Commercial Agreement
     * @return Commercial Agreement
     */
    @Transactional
    public CommercialAgreement createOrUpdateAgreement(final CommercialAgreement model) {
        // Start by grabbing the existing object from the database
        Optional<CommercialAgreement> existingModelOpt = commercialAgreementRepo.findByNumber(model.getNumber());

        if (existingModelOpt.isPresent()) {
            // This is an update request - amend the data of the existing object with any requested amends
            CommercialAgreement existingModel = existingModelOpt.get();

            existingModel.setName(model.getName());
            existingModel.setOwner(model.getOwner());
            existingModel.setDescription(model.getDescription());
            existingModel.setStartDate(model.getStartDate());
            existingModel.setEndDate(model.getEndDate());
            existingModel.setDetailUrl(model.getDetailUrl());
            existingModel.setPreDefinedLotRequired(model.getPreDefinedLotRequired());
            existingModel.setRegulation(model.getRegulation());
            existingModel.setAgreementType(model.getAgreementType());

            if (model.getBenefits() != null && !model.getBenefits().isEmpty() ){
                commercialAgreementBenefitService.removeBenefits(existingModel);
                model.getBenefits().forEach(existingModel::addBenefit);
            }

            // Model is now updated, so trigger the save
            commercialAgreementRepo.saveAndFlush(existingModel);
        } else {
            // This is a create request - we just need to configure any benefits correctly then trigger saving
            if (model.getBenefits() != null && !model.getBenefits().isEmpty()){
                model.getBenefits().forEach(benefit -> {
                    benefit.setAgreement(model);
                });
            }

            commercialAgreementRepo.saveAndFlush(model);
        }

        // Now fetch a fresh copy to return
        return findAgreementByNumber(model.getNumber());
    }

    /**
     * Create or update the lot that is passed in
     *
     * @param newLot Lot
     * @return Lot
     */
    public Lot createOrUpdateLot(final Lot newLot) {
        String agreementNumber =newLot.getAgreement().getNumber();
        String lotNumber = newLot.getNumber();
        return lotRepo.findByAgreementNumberAndNumber(agreementNumber, lotNumber)
                .map(lot -> {
                    lot.setName(newLot.getName());
                    lot.setDescription(newLot.getDescription());
                    lot.setLotType(newLot.getLotType());
                    lot.setStartDate(newLot.getStartDate());
                    lot.setEndDate(newLot.getEndDate());

                    lotRepo.saveAndFlush(lot);
                    return findLotByAgreementNumberAndLotNumber(agreementNumber, lotNumber);
                }).orElseGet(() -> {
                    lotRepo.saveAndFlush(newLot);
                    return findLotByAgreementNumberAndLotNumber(agreementNumber, lotNumber);
                });
    }

  /**
   * Find a specific Lot using Agreement Number and Lot Number.
   *
   * @param agreementNumber Commercial Agreement number
   * @param lotNumber Lot number
   * @return Lot
   */
  public Lot findLotByAgreementNumberAndLotNumber(final String agreementNumber, final String lotNumber) {
    log.debug("findLotByAgreementNumberAndLotNumber: agreementNumber={},lotNumber={}", agreementNumber, lotNumber);

      return lotRepo.findByAgreementNumberAndNumber(agreementNumber, lotNumber).orElseThrow(() -> new LotNotFoundException(agreementNumber,lotNumber) );
  }

  /**
   * Get Lot by id.
   *
   * @param lotId Lot id
   * @return Lot
   */
  public Lot getLot(final Integer lotId) {
    log.debug("getLot: {}", lotId);
    return lotRepo.getOne(lotId);
  }

  /**
   * Find Event Type by Name.
   *
   * @return ProcurementEventType
  */
  public ProcurementEventType findEventTypeByName(final String procurementEventTypeName) {
    log.debug("findEventTypeByName by name: {}", procurementEventTypeName);
    return procurementEventTypeRepo.findByName(procurementEventTypeName);
  }

  /**
   * Find all lot supplier organisation roles
   *
   * @param agreementNumber Commercial Agreement number
   * @param lotNumber Lot number
   * @return collection of lot supplier org roles
   * @throws LotNotFoundException if CA or lot not found
   * */
  public Collection<LotOrganisationRole> findLotSupplierOrgRolesByAgreementNumberAndLotNumber(
      final String agreementNumber, final String lotNumber) {

    final SimpleLot lot = simpleLotRepo.findByAgreementNumberAndNumber(agreementNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }

    return lot.getActiveOrganisationRoles();
  }

  /**
   * Update the event types for the given lot.
   *
   * @param lotModel                    Lot
   * @param eventType                   ProcurementEventType
   * @param lotEventTypeUpdate          LotEventTypeUpdate
  */
  public LotProcurementEventType updateLotEventTypes(final Lot lotModel, final ProcurementEventType eventType, LotEventTypeUpdate lotEventTypeUpdate) {
      LotProcurementEventTypeKey lotProcurementEventTypeKey = new LotProcurementEventTypeKey();
      lotProcurementEventTypeKey.setLotId(lotModel.getId());
      lotProcurementEventTypeKey.setProcurementEventTypeId(eventType.getId());

      LotProcurementEventType lotProcurementEventType = new LotProcurementEventType();
      lotProcurementEventType.setKey(lotProcurementEventTypeKey);
      lotProcurementEventType.setLot(lotModel);
      lotProcurementEventType.setProcurementEventType(eventType);

      if (lotEventTypeUpdate != null && lotEventTypeUpdate.getMandatoryEvent() != null) {
          lotProcurementEventType.setIsMandatoryEvent(lotEventTypeUpdate.getMandatoryEvent());
      } else {
          // Set to the correct default value, as fallback.
          lotProcurementEventType.setIsMandatoryEvent(false);
      }

      if (lotEventTypeUpdate != null && lotEventTypeUpdate.getRepeatableEvent() != null) {
          lotProcurementEventType.setIsRepeatableEvent(lotEventTypeUpdate.getRepeatableEvent());
      } else {
          // Set to the correct default value, as fallback.
          lotProcurementEventType.setIsRepeatableEvent(true);
      }

      if (lotEventTypeUpdate != null && lotEventTypeUpdate.getMaxRepeats() != null) {
          lotProcurementEventType.setMaxRepeats(lotEventTypeUpdate.getMaxRepeats());
      } else {
          // Set to the correct default value, as fallback.
          lotProcurementEventType.setMaxRepeats(5);
      }

      if (lotEventTypeUpdate != null && lotEventTypeUpdate.getAssessmentToolId() != null) {
          lotProcurementEventType.setAssessmentToolId(lotEventTypeUpdate.getAssessmentToolId());
      } else {
          // Set to the correct default value, as fallback.
          lotProcurementEventType.setAssessmentToolId(null);
      }

      lotProcurementEventTypeRepo.saveAndFlush(lotProcurementEventType);

      return lotProcurementEventType;
  }

  /**
   * Create or update the procurement data template that is passed in
   *
   * @param updateProcurementDataTemplate ProcurementQuestionTemplate
   * @return ProcurementQuestionTemplate
   */
  public ProcurementQuestionTemplate createOrUpdateProcurementDataTemplate(final ProcurementQuestionTemplate updateProcurementDataTemplate) {
      return procurementQuestionTemplateRepo.findById(updateProcurementDataTemplate.getId())
          .map(procurementDataTemplate -> {
              procurementDataTemplate.setTemplateName(updateProcurementDataTemplate.getTemplateName());
              procurementDataTemplate.setTemplatePayload(updateProcurementDataTemplate.getTemplatePayload());
              procurementDataTemplate.setDescription(updateProcurementDataTemplate.getDescription());
              procurementDataTemplate.setParent(updateProcurementDataTemplate.getParent());
              procurementDataTemplate.setTemplateUrl(updateProcurementDataTemplate.getTemplateUrl());
              procurementDataTemplate.setMandatory(updateProcurementDataTemplate.getMandatory());
              procurementDataTemplate.setUpdatedAt(LocalDateTime.now());

              procurementQuestionTemplateRepo.saveAndFlush(procurementDataTemplate);
              return findByTemplateId(procurementDataTemplate.getId());
          }).orElseGet(() -> {
              updateProcurementDataTemplate.setCreatedAt(LocalDateTime.now());
              updateProcurementDataTemplate.setUpdatedAt(LocalDateTime.now());
              procurementQuestionTemplateRepo.saveAndFlush(updateProcurementDataTemplate);
              return findByTemplateId(updateProcurementDataTemplate.getId());
          });
  }

    /**
     * Find a specific procurement questions template using Template ID.
     *
     * @param templateId Database ID of the procurement questions template
     * @return ProcurementQuestionTemplate
     */
    public ProcurementQuestionTemplate findByTemplateId(final Integer templateId) {
        log.debug("findByTemplateId: templateId={}", templateId);

        return procurementQuestionTemplateRepo.findById(templateId).orElseThrow(() -> new ProcurementQuestionTemplateNotFoundException(templateId) );
    }
}