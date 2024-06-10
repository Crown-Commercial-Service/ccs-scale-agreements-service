package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class SupplierSummary implements Serializable {
    
    /**
     * Last updated date
     */
    private LocalDate lastUpdatedDate;

    /**
     * Last updated by
     */
    private String lastUpdatedBy;

    /**
     * supplier count for given lot/agreement
     */
    private int supplierCount;

    public SupplierSummary(LocalDate lastUpdatedDate, String lastUpdatedBy, int supplierCount) {
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.supplierCount = supplierCount;
    }

    public SupplierSummary() {
    }
}


