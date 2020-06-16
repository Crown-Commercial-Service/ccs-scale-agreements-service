package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementRepo;
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
   * Get a specific agreement.
   * 
   * @param caNumber Commercial Agreement number
   * @return CommercialAgreement
   */
  public CommercialAgreement getAgreement(final String caNumber) {
    log.debug("getAgreement: {}", caNumber);
    return commercialAgreementRepo.findAgreementByNumber(caNumber);
  }

  /**
   * Get a specific Lot.
   * 
   * @param caNumber Commercial Agreement number
   * @param lotNumber Lot number
   * @return Lot
   */
  public Lot getLot(final String caNumber, final String lotNumber) {
    log.debug("getLot: {},{}", caNumber, lotNumber);
    return lotRepo.findLotByNumber(caNumber, lotNumber);
  }

}
