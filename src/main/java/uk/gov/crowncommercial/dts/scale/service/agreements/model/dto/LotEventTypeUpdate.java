package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotEventTypeUpdate {

  private String type;

  private Boolean mandatoryEvent;

  private Boolean repeatableEvent;

  private Integer maxRepeats;

  private String assessmentToolId;
}
