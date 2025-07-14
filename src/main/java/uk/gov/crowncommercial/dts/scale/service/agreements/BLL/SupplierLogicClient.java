package uk.gov.crowncommercial.dts.scale.service.agreements.BLL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierUpdateRequest;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatus;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.SupplierService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

@Component
public class SupplierLogicClient {
    @Autowired
    private SupplierService supplierService;

    @Autowired
    private AgreementService agreementService;

    public void updateSupplierByDuns(String dunsNumber, SupplierUpdateRequest updateRequest) {
        supplierService.updateSupplierAndContactByDuns(dunsNumber, updateRequest);
    }

    public SupplierSummary addSupplierToLotByDuns(String agreementId, String lotId, String dunsNumber) {
        Organisation organisation = supplierService.findOrganisationBySchemeAndEntityId("US-DUNS", dunsNumber);
        Lot lot = agreementService.findLotByAgreementNumberAndLotNumber(agreementId, lotId);
        supplierService.addSupplierRelationship(lot, organisation, null, "api", SupplierStatus.ACTIVE);
        int supplierCount = supplierService.getLotSuppliersCount(agreementId, lotId);
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