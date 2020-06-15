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

  public List<CommercialAgreement> getAgreements() {
    log.debug("getAgreements");
    return commercialAgreementRepo.findAll();
  }

  public CommercialAgreement getAgreement(final String caNumber) {
    log.debug("getAgreement: {}", caNumber);
    return commercialAgreementRepo.findAgreementByNumber(caNumber);
  }

  public Lot getLot(final String caNumber, final String lotNumber) {
    log.debug("getLot: {},{}", caNumber, lotNumber);
    return lotRepo.findLotByNumber(caNumber, lotNumber);
  }

}
