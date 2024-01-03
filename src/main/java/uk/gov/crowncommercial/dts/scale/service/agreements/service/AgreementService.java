package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.LotRepo;
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
     * @param newCommercialAgreement Commercial Agreement
     * @return Commercial Agreement
     */
  public CommercialAgreement createOrUpdateAgreement(final CommercialAgreement newCommercialAgreement) {
    return commercialAgreementRepo.findByNumber(newCommercialAgreement.getNumber())
            .map(ca -> {
              ca.setName(newCommercialAgreement.getName());
              ca.setOwner(newCommercialAgreement.getOwner());
              ca.setDescription(newCommercialAgreement.getDescription());
              ca.setStartDate(newCommercialAgreement.getStartDate());
              ca.setEndDate(newCommercialAgreement.getEndDate());
              ca.setDetailUrl(newCommercialAgreement.getDetailUrl());
              ca.setPreDefinedLotRequired(newCommercialAgreement.getPreDefinedLotRequired());

              if (newCommercialAgreement.getBenefits() != null && !newCommercialAgreement.getBenefits().isEmpty() ){
                  commercialAgreementBenefitService.removeBenefits(ca);
                  newCommercialAgreement.getBenefits().forEach(benefit -> {
                      ca.addBenefit(benefit);
                  });
              }

              commercialAgreementRepo.saveAndFlush(ca);
              return findAgreementByNumber(ca.getNumber());
            }).orElseGet(() -> {
                if (newCommercialAgreement.getBenefits() != null && !newCommercialAgreement.getBenefits().isEmpty() ){
                    newCommercialAgreement.getBenefits().forEach(benefit -> {
                        benefit.setAgreement(newCommercialAgreement);
                    });
                }
                commercialAgreementRepo.saveAndFlush(newCommercialAgreement);
                return findAgreementByNumber(newCommercialAgreement.getNumber());
            });
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
   */
  public Collection<LotOrganisationRole> findLotSupplierOrgRolesByAgreementNumberAndLotNumber(
      final String agreementNumber, final String lotNumber) {

    final SimpleLot lot = simpleLotRepo.findByAgreementNumberAndNumber(agreementNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }

    return lot.getActiveOrganisationRoles();
  }

}
