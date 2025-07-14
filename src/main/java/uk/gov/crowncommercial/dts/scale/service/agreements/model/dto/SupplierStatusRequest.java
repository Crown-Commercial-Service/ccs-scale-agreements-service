package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierStatusRequest {

    @NotBlank(message = "operation must not be blank")
    private String operation;
} 