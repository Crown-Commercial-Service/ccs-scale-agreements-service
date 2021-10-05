package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Agreement Controller.
 *
 */
@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
@Slf4j
public class AgreementController {

  private final AgreementService service;
  private final AgreementConverter converter;

  @GetMapping
  public Collection<AgreementSummary> getAgreements() {
    log.debug("getAgreements");
    final List<CommercialAgreement> agreements = service.getAgreements();
    return agreements.stream().map(converter::convertAgreementToSummaryDTO)
        .collect(Collectors.toList());
  }

  @GetMapping("/{agreement-id}")
  public AgreementDetail getAgreementDetail(
      @PathVariable(value = "agreement-id") final String agreementId) {
    log.debug("getAgreement: {}", agreementId);
    final CommercialAgreement ca = getAgreement(agreementId);
    return converter.convertAgreementToDTO(ca);
  }

  @GetMapping("/{agreement-id}/lots")
  public Collection<LotDetail> getAgreementLots(
      @PathVariable(value = "agreement-id") final String agreementId,
      @RequestParam Optional<BuyingMethod> buyingMethod) {
    log.debug("getAgreementLots: agreementId={}, buyingMethod={}", agreementId, buyingMethod);
    final CommercialAgreement ca = getAgreement(agreementId);
    Collection<LotDetail> lots = converter.convertLotsToDTOs(ca.getLots());
    if (buyingMethod.isPresent()) {
      return filterLotsByBuyingMethod(lots, buyingMethod.get());
    } else {
      return lots;
    }
  }

  @GetMapping("/{agreement-id}/documents")
  public Collection<Document> getAgreementDocuments(
      @PathVariable(value = "agreement-id") final String agreementId) {
    log.debug("getAgreementDocuments: {}", agreementId);
    final CommercialAgreement ca = getAgreement(agreementId);
    return converter.convertAgreementDocumentsToDTOs(ca.getDocuments());
  }

  @GetMapping("/{agreement-id}/updates")
  public Collection<AgreementUpdate> getAgreementUpdates(
      @PathVariable(value = "agreement-id") final String agreementId) {
    log.debug("getAgreementUpdates: {}", agreementId);
    final CommercialAgreement ca = getAgreement(agreementId);
    return converter.convertAgreementUpdatesToDTOs(ca.getUpdates());
  }

  private CommercialAgreement getAgreement(final String caNumber) {
    final CommercialAgreement ca = service.findAgreementByNumber(caNumber);
    if (ca == null) {
      throw new AgreementNotFoundException(caNumber);
    }
    return ca;
  }

  private Collection<LotDetail> filterLotsByBuyingMethod(Collection<LotDetail> lots,
      BuyingMethod buyingMethod) {
    return lots.stream()
        .filter(ld -> ld.getRoutesToMarket().stream()
            .filter(rtm -> buyingMethod == rtm.getBuyingMethod()).count() > 0)
        .collect(Collectors.toSet());
  }

}
