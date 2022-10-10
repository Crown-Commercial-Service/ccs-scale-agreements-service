package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

@Data
public class QuestionTemplate {
    private Integer templateGroupId;
    private Integer templateId;
    private String name;
    private String description;
    private Boolean mandatoryTemplate;
    private Integer inheritsFrom;
}
