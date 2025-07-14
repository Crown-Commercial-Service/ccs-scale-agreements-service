package uk.gov.crowncommercial.dts.scale.service.agreements.BLL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotSupplier;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierUpdateRequest;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatus;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.SupplierService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SupplierLogicClient {
    @Autowired
    private SupplierService supplierService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private uk.gov.crowncommercial.dts.scale.service.agreements.service.MappingService mappingService;

    /**
     * Returns a list of LotSupplier objects which relate to a specified lot / agreement
     */
    @Cacheable(value = "getLotSuppliers", key="#agreementId + #lotId")
    public Collection<LotSupplier> getLotSuppliers(String agreementId, String lotId) {
        Collection<LotSupplier> model = null;

        // Fetch a list of LotOrganisationRoles from the service
        Collection<LotOrganisationRole> lotOrgRoles = agreementService.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(agreementId, lotId);

        if (lotOrgRoles != null) {
            // Now convert the items we've found into the format we want to return
            model = lotOrgRoles.stream().map(mappingService::mapLotOrganisationRoleToLotSupplier).collect(Collectors.toList());
        }

        return model;
    }

    public void updateSupplierByDuns(String dunsNumber, SupplierUpdateRequest updateRequest) {
        supplierService.updateSupplierAndContactByDuns(dunsNumber, updateRequest);
    }

    public SupplierSummary addSupplierToLotByDuns(String agreementId, String lotId, String dunsNumber) {
        Organisation organisation = supplierService.findOrganisationBySchemeAndEntityId("US-DUNS", dunsNumber);
        Lot lot = agreementService.findLotByAgreementNumberAndLotNumber(agreementId, lotId);
        supplierService.addSupplierRelationship(lot, organisation, null, "api", SupplierStatus.ACTIVE);
        int supplierCount = getLotSuppliers(agreementId, lotId).size();
        return new SupplierSummary(java.time.LocalDate.now(), "api", supplierCount);
    }

    public void updateSupplierStatusForLot(String agreementId, String lotId, String dunsNumber, String operation) {
        Organisation organisation = supplierService.findOrganisationBySchemeAndEntityId("US-DUNS", dunsNumber);
        Lot lot = agreementService.findLotByAgreementNumberAndLotNumber(agreementId, lotId);
        SupplierStatus status;
        if ("suspend".equalsIgnoreCase(operation)) {
            status = SupplierStatus.SUSPENDED;
        } else if ("unsuspend".equalsIgnoreCase(operation)) {
            status = SupplierStatus.ACTIVE;
        } else {
            throw new IllegalArgumentException("Invalid operation: " + operation);
        }
        supplierService.updateSupplierStatusForLot(lot, organisation, status);
    }


} 