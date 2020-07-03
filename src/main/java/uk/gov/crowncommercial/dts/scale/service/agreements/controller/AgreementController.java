package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.MethodNotImplementedException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.ResourceNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
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
    List<CommercialAgreement> agreements = service.getAgreements();
    return agreements.stream().map(converter::convertAgreementToSummaryDTO)
        .collect(Collectors.toList());
  }

  @GetMapping("/agreements/{ca-number}")
  public AgreementDetail getAgreement(@PathVariable(value = "ca-number") String caNumber) {
    log.debug("getAgreement: {}", caNumber);
    CommercialAgreement ca = service.findAgreementByNumber(caNumber);
    if (ca == null) {
      throw new ResourceNotFoundException(
          String.format("Agreement number '%s' not found", caNumber));
    }
    return converter.convertAgreementToDTO(ca);
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}")
  public LotDetail getLot(@PathVariable(value = "ca-number") String caNumber,
      @PathVariable(value = "lot-number") String lotNumber) {
    log.debug("getLot: caNumber={},lotNumber={}", caNumber, lotNumber);
    Lot lot = service.findLotByAgreementNumberAndLotNumber(caNumber, lotNumber);
    if (lot == null) {
      throw new ResourceNotFoundException(String
          .format("Lot number '%s' for agreement number '%s' not found", lotNumber, caNumber));
    }
    return converter.convertLotToDTO(lot);
  }

  @GetMapping("/agreements/{ca-number}/documents")
  public Collection<Document> getAgreementDocuments(
      @PathVariable(value = "ca-number") String caNumber) {
    log.debug("getAgreementDocuments: {}", caNumber);
    throw new MethodNotImplementedException(METHOD_NOT_IMPLEMENTED_MSG);
  }

  @GetMapping("/agreements/{ca-number}/updates")
  public Collection<AgreementUpdate> getAgreementUpdates(
      @PathVariable(value = "ca-number") String caNumber) {
    log.debug("getAgreementUpdates: {}", caNumber);
    throw new MethodNotImplementedException(METHOD_NOT_IMPLEMENTED_MSG);
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}/suppliers")
  public Collection<Organisation> getLotSuppliers(
      @PathVariable(value = "ca-number") String caNumber,
      @PathVariable(value = "lot-number") String lotNumber) {
    log.debug("getLotSuppliers: caNumber={}, lotNumber={}", caNumber, lotNumber);
    throw new MethodNotImplementedException(METHOD_NOT_IMPLEMENTED_MSG);
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}/documents")
  public Collection<Document> getLotDocuments(@PathVariable(value = "ca-number") String caNumber,
      @PathVariable(value = "lot-number") String lotNumber) {
    log.debug("getLotDocuments: caNumber={}, lotNumber={}", caNumber, lotNumber);
    throw new MethodNotImplementedException(METHOD_NOT_IMPLEMENTED_MSG);
  }

}
