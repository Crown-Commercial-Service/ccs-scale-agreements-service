package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.EventType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementEventType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementEventType;

class EventTypeConverterTest {

  public static final String EXPRESSION_OF_INTEREST = "Expression Of Interest";
  public static final String EOI = "EOI";
  public static final boolean IS_PRE_MARKET_EVENT = false;

  private EventTypeConverter converter= new EventTypeConverter();

  @Test
  void testLotDetailProductCollection() {
    ProcurementEventType procurementEventType = new ProcurementEventType();
    procurementEventType.setName(EOI);
    procurementEventType.setDescription(EXPRESSION_OF_INTEREST);
    procurementEventType.setIsPreMarketEvent(IS_PRE_MARKET_EVENT);
    LotProcurementEventType lotEventType = new LotProcurementEventType();
    lotEventType.setProcurementEventType(procurementEventType);

    EventType eventType = converter.convert(lotEventType);
    assertEquals(EOI,eventType.getType());
    assertEquals(EXPRESSION_OF_INTEREST,eventType.getDescription());
    assertEquals(IS_PRE_MARKET_EVENT,eventType.getIsPreMarketEvent());
  }

}
