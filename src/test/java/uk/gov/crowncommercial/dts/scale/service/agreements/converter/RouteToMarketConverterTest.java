package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.BuyingMethod;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RouteToMarketDTO;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.RouteToMarket;

public class RouteToMarketConverterTest {

  RouteToMarketConverter converter = new RouteToMarketConverter();

  @Test
  public void testBuyingMethodDirectAward() {
    assertEquals(BuyingMethod.DIRECT_AWARD, createRouteToMarket("Direct Award").getBuyingMethod());
  }

  @Test
  public void testBuyingMethodDirectAwardCaseInsensitive() {
    assertEquals(BuyingMethod.DIRECT_AWARD, createRouteToMarket("DIRECT aWARD").getBuyingMethod());
  }

  @Test
  public void testBuyingMethodEAuction() {
    assertEquals(BuyingMethod.E_AUCTION, createRouteToMarket("Auction").getBuyingMethod());
  }

  @Test
  public void testBuyingMethodMarketPlace() {
    assertEquals(BuyingMethod.MARKETPLACE, createRouteToMarket("Marketplace").getBuyingMethod());
  }

  @Test
  public void testBuyingMethodFurtherCompetition() {
    assertEquals(BuyingMethod.FURTHER_COMPETITION,
        createRouteToMarket("Further Competition").getBuyingMethod());
  }

  @Test
  public void testBuyingMethodUnknown() {
    assertNull(createRouteToMarket("Unknown").getBuyingMethod());
  }

  private RouteToMarketDTO createRouteToMarket(String routeToMarketName) {
    RouteToMarket rtm = new RouteToMarket();
    rtm.setName(routeToMarketName);
    LotRouteToMarket lrtm = new LotRouteToMarket();
    lrtm.setRouteToMarket(rtm);
    return converter.convert(lrtm);
  }
}
