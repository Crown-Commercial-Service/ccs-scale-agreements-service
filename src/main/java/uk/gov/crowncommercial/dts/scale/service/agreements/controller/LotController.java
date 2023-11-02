package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;

import java.util.Collection;

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

  @GetMapping
  public LotDetail getLot(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) {
    log.debug("getLot called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    LotDetail model = businessLogicClient.getLotDetail(agreementNumber, lotNumber);

    return model;
  }

  @GetMapping("/suppliers")
  public Collection<LotSupplier> getLotSuppliers(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) throws JsonProcessingException {
    log.debug("getLotSuppliers called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    Collection<LotSupplier> model = businessLogicClient.getLotSuppliers(agreementNumber, lotNumber);
    ObjectMapper mapper= new ObjectMapper();

    JSONObject test = new JSONObject(mapper.writeValueAsString(model));
    return model;
  }

  @GetMapping("/event-types")
  public Collection<EventType> getLotEventTypes(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) {
    log.debug("getLotEventTypes called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    Collection<EventType> model = businessLogicClient.getLotEventTypes(agreementNumber, lotNumber);

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

  @PutMapping("/suppliers")
  public Collection<LotSupplier> putLotSuppliers(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber, @RequestBody LotSupplier updatedLotSupplier) {
    log.debug("putLotSuppliers called with values: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    Collection<LotSupplier> model = businessLogicClient.getLotSuppliers(agreementNumber, lotNumber);

    return model;
  }
}