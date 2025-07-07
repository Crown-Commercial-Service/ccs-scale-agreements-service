package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * DTO for updating supplier/organisation details.
 */
@Data
public class SupplierUpdateRequest implements Serializable {
    private String emailAddress;
    private String contactPointName;
    private String telephoneNumber;
    private String streetAddress;
    private String locality;
    private String postalCode;
} 