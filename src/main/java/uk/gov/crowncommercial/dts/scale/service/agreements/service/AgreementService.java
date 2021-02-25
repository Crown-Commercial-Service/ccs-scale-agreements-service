package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementDocument;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementDocumentRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementUpdateRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.LotRepo;

/**
 * Agreement Service.
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementService {

  private final CommercialAgreementRepo commercialAgreementRepo;
  private final CommercialAgreementUpdateRepo commercialAgreementUpdateRepo;
  private final CommercialAgreementDocumentRepo commercialAgreementDocumentRepo;
  private final LotRepo lotRepo;

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
    return commercialAgreementRepo.findByNumber(caNumber);
  }

  /**
   * Find all Lots using Agreement Number.
   * 
   * @param caNumber Commercial Agreement number
   * @return list of Lots
   */
  public Collection<Lot> findLotsByAgreementNumber(final String caNumber) {
    log.debug("findLotsByAgreementNumber: caNumber={}", caNumber);
    return lotRepo.findByAgreementNumber(caNumber);
  }

  /**
   * Find a specific Lot using Agreement Number and Lot Number.
   * 
   * @param caNumber Commercial Agreement number
   * @param lotNumber Lot number
   * @return Lot
   */
  public Lot findLotByAgreementNumberAndLotNumber(final String caNumber, final String lotNumber) {
    log.debug("findLotByAgreementNumberAndLotNumber: caNumber={},lotNumber={}", caNumber,
        lotNumber);
    return lotRepo.findByAgreementNumberAndNumber(caNumber, lotNumber);
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
   * Find all updates for an agreement.
   * 
   * @param caNumber Commercial Agreement number
   * @return CommercialAgreement
   */
  public Collection<CommercialAgreementUpdate> findAgreementUpdatesByAgreementNumber(
      final String caNumber) {
    log.debug("findAgreementUpdatesByAgreementNumber: {}", caNumber);
    return commercialAgreementUpdateRepo.findByAgreementNumber(caNumber);
  }

  /**
   * Find all documents for an agreement.
   * 
   * @param caNumber Commercial Agreement number
   * @return CommercialAgreement
   */
  public Collection<CommercialAgreementDocument> findAgreementDocumentsByAgreementNumber(
      final String caNumber) {
    log.debug("findAgreementDocumentsByAgreementNumber: {}", caNumber);
    return commercialAgreementDocumentRepo.findByAgreementNumber(caNumber);
  }
}
