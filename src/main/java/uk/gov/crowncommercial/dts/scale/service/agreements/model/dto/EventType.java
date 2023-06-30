package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * EventType
 */
@Data
public class EventType implements Serializable {

  private String type;

  private String description;

  private Boolean preMarketActivity;

  private String assessmentToolId;

  private Boolean mandatoryEventInd;

  private Collection<QuestionTemplate> templateGroups;
}
