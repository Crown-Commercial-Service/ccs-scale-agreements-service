package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

/**
 * EventType
 */
@Data
public class EventType {

  private String type;

  private String description;

  private  Boolean isPreMarketEvent;

}
