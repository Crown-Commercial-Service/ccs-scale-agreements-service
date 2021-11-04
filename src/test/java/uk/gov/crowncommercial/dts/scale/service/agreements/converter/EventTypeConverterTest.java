package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.EventType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementEventType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ProcurementEventType;

class EventTypeConverterTest {

  public static final String EXPRESSION_OF_INTEREST = "Expression Of Interest";
  public static final String EOI = "EOI";
  public static final boolean IS_PRE_MARKET_ACTIVITY = false;

  private EventTypeConverter converter= new EventTypeConverter();

  @Test
  void testLotDetailProductCollection() {
    ProcurementEventType procurementEventType = new ProcurementEventType();
    procurementEventType.setName(EOI);
    procurementEventType.setDescription(EXPRESSION_OF_INTEREST);
    procurementEventType.setPreMarketActivity(IS_PRE_MARKET_ACTIVITY);
    LotProcurementEventType lotEventType = new LotProcurementEventType();
    lotEventType.setProcurementEventType(procurementEventType);

    EventType eventType = converter.convert(lotEventType);
    assertEquals(EOI,eventType.getType());
    assertEquals(EXPRESSION_OF_INTEREST,eventType.getDescription());
    assertEquals(IS_PRE_MARKET_ACTIVITY,eventType.getPreMarketActivity());
  }

}
