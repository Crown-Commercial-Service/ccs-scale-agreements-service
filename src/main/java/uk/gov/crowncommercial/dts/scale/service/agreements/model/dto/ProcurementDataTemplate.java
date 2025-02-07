package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProcurementDataTemplate implements Serializable {
    private Integer id;
    private String templateName;
    private Integer parent;
    private Boolean mandatory;
    private Object criteria;
    private String createdBy;
}
