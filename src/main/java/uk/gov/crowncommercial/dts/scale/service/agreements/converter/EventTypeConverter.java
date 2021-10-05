package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.EventType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementEventType;

/**
 * Converts LotProcurementEventType from database into EventType.
 *
 */
@RequiredArgsConstructor
@Component
public class EventTypeConverter extends AbstractConverter<LotProcurementEventType, EventType> {

  @Override
  protected EventType convert(LotProcurementEventType lotProcurementEventType) {
    EventType eventType = new EventType();
    eventType.setType(lotProcurementEventType.getProcurementEventType().getName());
    // TODO: Description needs to be added to the database table - see SCAT-1767
    // eventType.setDescription(null);
    return eventType;
  }

}
