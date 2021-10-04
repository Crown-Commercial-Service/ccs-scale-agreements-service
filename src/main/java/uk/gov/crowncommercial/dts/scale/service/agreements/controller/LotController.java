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

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}/suppliers")
  public Collection<LotSupplier> getLotSuppliers(
      @PathVariable(value = "ca-number") final String caNumber,
      @PathVariable(value = "lot-number") final String lotNumber) {

    log.debug("getLotSuppliers: caNumber={}, lotNumber={}", caNumber, lotNumber);

    final Collection<LotOrganisationRole> lotOrgRoles =
        service.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(caNumber, lotNumber);

    return converter.convertLotOrgRolesToLotSupplierDTOs(lotOrgRoles);
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}/event-types")
  public Collection<EventType> getLotEventTypes(
      @PathVariable(value = "ca-number") final String caNumber,
      @PathVariable(value = "lot-number") final String lotNumber) {

    log.debug("getLotEventTypes: caNumber={}, lotNumber={}", caNumber, lotNumber);

    final Lot lot = service.findLotByAgreementNumberAndLotNumber(caNumber, lotNumber);
    if (lot == null) {
      throw new LotNotFoundException(lotNumber, caNumber);
    }

    return converter.convertLotProcurementEventTypesToDTOs(lot.getProcurementEventTypes());
  }

}
