package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.SupplierLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;

import java.util.Collection;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatusRequest;
import uk.gov.crowncommercial.dts.scale.service.agreements.util.OcdsRequirementsCsvExporter;

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

  @Autowired
  private OcdsRequirementsCsvExporter ocdsRequirementsCsvExporter;

  @GetMapping
  public LotDetail getLot(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) {
    log.debug("getLot called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

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

  @GetMapping("/event-types/{event-type}/data-templates/ocds-requirements/csv")
  public ResponseEntity<String> getOcdsRequirementsAsCsv(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber, @PathVariable(value = "event-type") final String eventType) {
    log.debug("getOcdsRequirementsAsCsv called with values: agreementNumber={}, lotNumber={}, eventType={}", agreementNumber, lotNumber, eventType);

    try {
      Collection<ProcurementDataTemplate> dataTemplates = businessLogicClient.getEventDataTemplates(agreementNumber, lotNumber, eventType);
      
      if (dataTemplates == null || dataTemplates.isEmpty()) {
        return ResponseEntity.notFound().build();
      }

      String csvData = ocdsRequirementsCsvExporter.exportToCsv(dataTemplates);
      
      return ResponseEntity.ok()
          .header("Content-Type", "text/csv")
          .header("Content-Disposition", "attachment; filename=\"ocds-requirements-" + agreementNumber + "-" + lotNumber + "-" + eventType + ".csv\"")
          .body(csvData);
          
    } catch (Exception e) {
      log.error("Error generating OCDS requirements CSV for agreement={}, lot={}, eventType={}: {}", agreementNumber, lotNumber, eventType, e.getMessage());
      return ResponseEntity.internalServerError().body("Error generating CSV: " + e.getMessage());
    }
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