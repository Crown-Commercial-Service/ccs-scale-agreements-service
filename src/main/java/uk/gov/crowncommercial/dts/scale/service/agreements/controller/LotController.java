package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.TemplateGroupConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.helpers.WordpressHelpers;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.QuestionTemplateService;

import java.util.Collection;

/**
 * Lot Controller.
 *
 */
@RestController
@RequestMapping("/agreements/{agreement-id}/lots/{lot-id}")
@RequiredArgsConstructor
@Slf4j
public class LotController {
  @Value("${wordpressURL:https://webdev-cms.crowncommercial.gov.uk/}")
  private String wordpressURL;

  private final AgreementService service;
  private final AgreementConverter converter;
  private final WordpressHelpers wordpressHelpers;

  private final QuestionTemplateService questionTemplateService;
  private TemplateGroupConverter templateGroupConverter = new TemplateGroupConverter();

  @GetMapping
  public LotDetail getLot(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) {
    log.debug("getLot: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);
    final Lot lot = service.findLotByAgreementNumberAndLotNumber(agreementNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }

    JSONObject jsonData = wordpressHelpers.connectWordpress(wordpressURL + "wp-json/ccs/v1/frameworks/" + agreementNumber + "/lot/" + lotNumber.substring(lotNumber.indexOf(" ") + 1));

    if(jsonData != null) {
      String lotDescriptionFromWordpress = wordpressHelpers.validateAndLog("description", jsonData, agreementNumber);

      lot.setDescription(lotDescriptionFromWordpress != null ? lotDescriptionFromWordpress : " ");
    }

    return converter.convertLotToDTO(lot);
  }

  @GetMapping("/suppliers")
  public Collection<LotSupplier> getLotSuppliers(@PathVariable(value = "agreement-id") final String agreementNumber, @PathVariable(value = "lot-id") final String lotNumber) {

    log.debug("getLotSuppliers: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    final Collection<LotOrganisationRole> lotOrgRoles =
        service.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(agreementNumber, lotNumber);

    return converter.convertLotOrgRolesToLotSupplierDTOs(lotOrgRoles);
  }

  @GetMapping("/event-types")
  public Collection<EventType> getLotEventTypes(
      @PathVariable(value = "agreement-id") final String agreementNumber,
      @PathVariable(value = "lot-id") final String lotNumber) {

    log.debug("getLotEventTypes: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    final Lot lot = service.findLotByAgreementNumberAndLotNumber(agreementNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }

    Collection<EventType> result = converter.convertLotProcurementEventTypesToDTOs(lot.getProcurementEventTypes());
    templateGroupConverter.assignTemplates(lot, result);
    return result;
  }

  @GetMapping("/event-types/{event-type}/data-templates")
  public Collection<ProcurementDataTemplate> getDataTemplates(
          @PathVariable(value = "agreement-id") final String agreementNumber,
          @PathVariable(value = "lot-id") final String lotNumber,
          @PathVariable(value = "event-type") final String eventType) {

    log.debug("getDataTemplates: agreementNumber={}, lotNumber={}, eventType={}", agreementNumber,
            lotNumber, eventType);

    final Lot lot = service.findLotByAgreementNumberAndLotNumber(agreementNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }

    return questionTemplateService.getDataTemplates(lot, eventType);
  }

  @GetMapping("/event-types/{event-type}/document-templates")
  public Collection<Document> getDocumentTemplates(
      @PathVariable(value = "agreement-id") final String agreementNumber,
      @PathVariable(value = "lot-id") final String lotNumber,
      @PathVariable(value = "event-type") final String eventType) {

    log.debug("getDocumentTemplates: agreementNumber={}, lotNumber={}, eventType={}",
        agreementNumber, lotNumber, eventType);

    final Lot lot = service.findLotByAgreementNumberAndLotNumber(agreementNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }
    return converter.convertLotProcurementQuestionTemplateToDocumentTemplates(
        lot.getProcurementQuestionTemplates(), eventType);
  }
}