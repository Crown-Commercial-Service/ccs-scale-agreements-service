package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Agreement Controller.
 *
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AgreementController {

  static final String METHOD_NOT_IMPLEMENTED_MSG = "This method is not yet implemented by the API";

  private final AgreementService service;
  private final AgreementConverter converter;

  @GetMapping("/agreements")
  public Collection<AgreementSummary> getAgreements() {
    log.debug("getAgreements");
    final List<CommercialAgreement> agreements = service.getAgreements();
    return agreements.stream().map(converter::convertAgreementToSummaryDTO)
        .collect(Collectors.toList());
  }

  @GetMapping("/agreements/{ca-number}")
  public AgreementDetail getAgreementDetail(
      @PathVariable(value = "ca-number") final String caNumber) {
    log.debug("getAgreement: {}", caNumber);
    final CommercialAgreement ca = getAgreement(caNumber);
    return converter.convertAgreementToDTO(ca);
  }

  @GetMapping("/agreements/{ca-number}/lots")
  public Collection<LotDetail> getAgreementLots(
      @PathVariable(value = "ca-number") final String caNumber,
      @RequestParam Optional<BuyingMethod> buyingMethod) {
    log.debug("getAgreementLots: caNumber={}, buyingMethod={}", caNumber, buyingMethod);
    final CommercialAgreement ca = getAgreement(caNumber);
    Collection<LotDetail> lots = converter.convertLotsToDTOs(ca.getLots());
    if (buyingMethod.isPresent()) {
      return filterLotsByBuyingMethod(lots, buyingMethod.get());
    } else {
      return lots;
    }
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}")
  public LotDetail getLot(@PathVariable(value = "ca-number") final String caNumber,
      @PathVariable(value = "lot-number") final String lotNumber) {
    log.debug("getLot: caNumber={},lotNumber={}", caNumber, lotNumber);
    final Lot lot = service.findLotByAgreementNumberAndLotNumber(caNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, caNumber);
    }
    return converter.convertLotToDTO(lot);
  }

  @GetMapping("/agreements/{ca-number}/documents")
  public Collection<Document> getAgreementDocuments(
      @PathVariable(value = "ca-number") final String caNumber) {
    log.debug("getAgreementDocuments: {}", caNumber);
    final CommercialAgreement ca = getAgreement(caNumber);
    return converter.convertAgreementDocumentsToDTOs(ca.getDocuments());
  }

  @GetMapping("/agreements/{ca-number}/updates")
  public Collection<AgreementUpdate> getAgreementUpdates(
      @PathVariable(value = "ca-number") final String caNumber) {
    log.debug("getAgreementUpdates: {}", caNumber);
    final CommercialAgreement ca = getAgreement(caNumber);
    return converter.convertAgreementUpdatesToDTOs(ca.getUpdates());
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}/suppliers")
  public Collection<LotSupplier> getLotSuppliers(
      @PathVariable(value = "ca-number") final String caNumber,
      @PathVariable(value = "lot-number") final String lotNumber) {

    log.debug("getLotSuppliers: caNumber={}, lotNumber={}", caNumber, lotNumber);

    final Collection<LotOrganisationRole> lotOrgRoles =
        service.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(caNumber, lotNumber);

    return converter.convertLotOrgRolesToLotSupplierDTOs(lotOrgRoles);
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
