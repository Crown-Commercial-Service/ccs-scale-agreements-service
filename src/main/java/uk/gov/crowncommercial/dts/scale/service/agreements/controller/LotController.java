package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.SupplierLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatusRequest;

/**
 * Lot Controller
 */
@RestController
@RequestMapping("/agreements/{agreement-id}/lots/{lot-id}")
@RequiredArgsConstructor
@Slf4j
public class LotController {
  @Autowired
  private BusinessLogicClient businessLogicClient;

  @Autowired
  private SupplierLogicClient supplierLogicClient;

  private static final Pattern AGREEMENT_ID_PATTERN = Pattern.compile("^(RM[0-9]{1,10}(?:\\.[0-9]{1,5})?)$");
  private static final Pattern LOT_ID_PATTERN = Pattern.compile("^([0-9]{1,5}[a-zA-Z]?)$");

  @GetMapping
  public LotDetail getLot(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) {
    log.debug("getLot called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    // First check if the Agreement ID and Lot ID passed to us are in a valid format
    if (agreementNumber == null || !AGREEMENT_ID_PATTERN.matcher(agreementNumber).matches()) {
      throw new AgreementNotFoundException(agreementNumber);
    }

    if (lotNumber == null || !LOT_ID_PATTERN.matcher(lotNumber).matches()) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }

    // They are valid, so proceed with lookup
    LotDetail model = businessLogicClient.getLotDetail(agreementNumber, lotNumber);

    return model;
  }

  @GetMapping("/suppliers")
  public Collection<LotSupplier> getLotSuppliers(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) {
    log.debug("getLotSuppliers called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    Collection<LotSupplier> model = businessLogicClient.getLotSuppliers(agreementNumber, lotNumber);

    return model;
  }

  @GetMapping("/event-types")
  public Collection<EventType> getLotEventTypes(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) {
    log.debug("getLotEventTypes called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    Collection<EventType> model = businessLogicClient.getLotEventTypes(agreementNumber, lotNumber);

    return model;
  }

  @PutMapping("/event-types")
  public Collection<EventType> putLotEventTypes(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber, @RequestBody final LotEventTypeUpdate lotEventTypeUpdate) {
    log.debug("putLotEventTypes called with values: agreementNumber={}, lotNumber={}, eventType={}", agreementNumber, lotNumber, lotEventTypeUpdate.getType());

    Collection<EventType> model = businessLogicClient.updateLotEventTypes(agreementNumber, lotNumber, lotEventTypeUpdate);

    return model;
  }

  @GetMapping("/event-types/{event-type}/data-templates")
  public Collection<ProcurementDataTemplate> getDataTemplates(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber, @PathVariable(value = "event-type") final String eventType) {
    log.debug("getDataTemplates called with values: agreementNumber={}, lotNumber={}, eventType={}", agreementNumber, lotNumber, eventType);

    Collection<ProcurementDataTemplate> model = businessLogicClient.getEventDataTemplates(agreementNumber, lotNumber, eventType);

    return model;
  }

  @GetMapping("/event-types/{event-type}/document-templates")
  public Collection<Document> getDocumentTemplates(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber, @PathVariable(value = "event-type") final String eventType) {
    log.debug("getDocumentTemplates called with values: agreementNumber={}, lotNumber={}, eventType={}", agreementNumber, lotNumber, eventType);

    Collection<Document> model = businessLogicClient.getEventDocumentTemplates(agreementNumber, lotNumber, eventType);

    return model;
  }

  @PutMapping
  public LotDetail updateLot(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber, @RequestBody final LotDetail lotDetail) {
    log.debug("updateLot called with ID: {}", agreementNumber + "Lot: "+ lotNumber);

    return businessLogicClient.saveLot(lotDetail, agreementNumber, lotNumber);
  }

  @PutMapping("/suppliers")
  public SupplierSummary updateLotSuppliers(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber, @RequestBody final Set<LotSupplier> lotSuppliersSet) {
    log.debug("updateLotSuppliers called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    return businessLogicClient.saveLotSuppliers(agreementNumber, lotNumber, lotSuppliersSet);
  }

  @PostMapping("/suppliers/duns/{dunsNumber}")
  public SupplierSummary addSupplierToLotByDuns(
      @PathVariable(value = "agreement-id") final String agreementNumber,
      @PathVariable(value = "lot-id") final String lotNumber,
      @PathVariable String dunsNumber) {
    log.debug("addSupplierToLotByDuns called with values: agreementNumber={}, lotNumber={}, dunsNumber={}", agreementNumber, lotNumber, dunsNumber);
    return supplierLogicClient.addSupplierToLotByDuns(agreementNumber, lotNumber, dunsNumber);
  }

  @PatchMapping("/suppliers/duns/{dunsNumber}/status")
  public ResponseEntity<?> updateSupplierStatus(
      @PathVariable(value = "agreement-id") String agreementNumber,
      @PathVariable(value = "lot-id") String lotNumber,
      @PathVariable String dunsNumber,
      @RequestBody SupplierStatusRequest statusRequest) {
    if (statusRequest.getOperation() == null || statusRequest.getOperation().trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Operation must not be blank");
    }
    supplierLogicClient.updateSupplierStatusForLot(
        agreementNumber, lotNumber, dunsNumber, statusRequest.getOperation());
    return ResponseEntity.ok().build();
  }
}