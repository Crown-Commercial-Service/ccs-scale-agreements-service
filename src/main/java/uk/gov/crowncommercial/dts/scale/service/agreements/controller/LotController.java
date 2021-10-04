package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Lot Controller.
 *
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class LotController {

  private final AgreementService service;
  private final AgreementConverter converter;

  @GetMapping("/agreements/{agreement-id}/lots/{lot-id}")
  public LotDetail getLot(@PathVariable(value = "agreement-id") final String agreementId,
      @PathVariable(value = "lot-id") final String lotId) {
    log.debug("getLot: agreementId={},lotId={}", agreementId, lotId);
    final Lot lot = service.findLotByAgreementNumberAndLotNumber(agreementId, lotId);
    if (lot == null) {
      throw new LotNotFoundException(lotId, agreementId);
    }
    return converter.convertLotToDTO(lot);
  }

  @GetMapping("/agreements/{agreement-id}/lots/{lot-id}/suppliers")
  public Collection<LotSupplier> getLotSuppliers(
      @PathVariable(value = "agreement-id") final String agreementId,
      @PathVariable(value = "lot-id") final String lotId) {

    log.debug("getLotSuppliers: agreementId={}, lotId={}", agreementId, lotId);

    final Collection<LotOrganisationRole> lotOrgRoles =
        service.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(agreementId, lotId);

    return converter.convertLotOrgRolesToLotSupplierDTOs(lotOrgRoles);
  }

  @GetMapping("/agreements/{agreement-id}/lots/{lot-id}/event-types")
  public Collection<EventType> getLotEventTypes(
      @PathVariable(value = "agreement-id") final String agreementId,
      @PathVariable(value = "lot-id") final String lotId) {

    log.debug("getLotEventTypes: agreementId={}, lotId={}", agreementId, lotId);

    final Lot lot = service.findLotByAgreementNumberAndLotNumber(agreementId, lotId);
    if (lot == null) {
      throw new LotNotFoundException(lotId, agreementId);
    }

    return converter.convertLotProcurementEventTypesToDTOs(lot.getProcurementEventTypes());
  }

}
