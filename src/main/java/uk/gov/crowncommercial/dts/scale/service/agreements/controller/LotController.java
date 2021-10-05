package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.EventType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotSupplier;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementQuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Lot Controller.
 *
 */
@RestController
@RequestMapping("/agreements/{agreement-id}/lots/{lot-id}")
@RequiredArgsConstructor
@Slf4j
public class LotController {

  private final AgreementService service;
  private final AgreementConverter converter;

  @GetMapping
  public LotDetail getLot(@PathVariable(value = "agreement-id") final String agreementNumber,
      @PathVariable(value = "lot-id") final String lotNumber) {
    log.debug("getLot: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);
    final Lot lot = service.findLotByAgreementNumberAndLotNumber(agreementNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, agreementNumber);
    }
    return converter.convertLotToDTO(lot);
  }

  @GetMapping("/suppliers")
  public Collection<LotSupplier> getLotSuppliers(
      @PathVariable(value = "agreement-id") final String agreementNumber,
      @PathVariable(value = "lot-id") final String lotNumber) {

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

    return converter.convertLotProcurementEventTypesToDTOs(lot.getProcurementEventTypes());
  }

  @GetMapping("/event-types/{event-type}/data-templates")
  public Collection<Object> getDataTemplates(
      @PathVariable(value = "agreement-id") final String agreementNumber,
      @PathVariable(value = "lot-id") final String lotNumber,
      @PathVariable(value = "event-type") final String eventType) {

    log.debug("getDataTemplates: agreementNumber={}, lotNumber={}", agreementNumber, lotNumber);

    final Collection<LotProcurementQuestionTemplate> questions =
        service.findLotProcurementQuestionTemplates(agreementNumber, lotNumber, eventType);

    return converter.convertLotProcurementQuestionTemplateToString(questions);
  }

}
