package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.SimpleLot;
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

    final CommercialAgreement agreementModel = commercialAgreementRepo.findByNumber(caNumber);

    if (agreementModel == null) {
      throw new AgreementNotFoundException(caNumber);
    }

    return agreementModel;
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

    final Lot lotModel = lotRepo.findByAgreementNumberAndNumber(agreementNumber, lotNumber);

    if (lotModel == null) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }

    return lotModel;
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
