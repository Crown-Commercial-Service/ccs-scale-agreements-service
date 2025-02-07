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
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.LotRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.ProcurementQuestionTemplateRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.SimpleLotRepo;

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