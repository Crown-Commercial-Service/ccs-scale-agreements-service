package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RouteToMarketDTO;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarketKey;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.RouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;

@SpringBootTest
@ActiveProfiles("test")
public class AgreementConverterTest {

  private static final LocalDate START_DATE = LocalDate.now();
  private static final LocalDate END_DATE = LocalDate.now();

  private static final String AGREEMENT_NUMBER = "R001";
  private static final String AGREEMENT_NAME = "Agreement 1";
  private static final String AGREEMENT_DESCRIPTION = "Agreement description";
  private static final String AGREEMENT_URL = "Agreement url";

  private static final String LOT_NUMBER = "Lot 1";
  private static final String LOT_NAME = "Test Lot";
  private static final String LOT_DESCRIPTION = "Test Description";

  private static final String SECTOR_NAME = "Sector Name";

  private static final String ROUTE_TO_MARKET_NAME = "RTM Name";
  private static final String ROUTE_TO_MARKET_DESCRIPTION = "RTM Description";

  private static final String LOCATION = "Mordor";

  @Autowired
  AgreementConverter converter;

  @Test
  public void testAgreementDetail() {

    Set<Lot> lots = new HashSet<>();
    lots.add(createTestLot("Products"));

    CommercialAgreement ca = new CommercialAgreement();
    ca.setNumber(AGREEMENT_NUMBER);
    ca.setName(AGREEMENT_NAME);
    ca.setDescription(AGREEMENT_DESCRIPTION);
    ca.setStartDate(START_DATE);
    ca.setEndDate(END_DATE);
    ca.setDetailUrl(AGREEMENT_URL);
    ca.setLots(lots);

    AgreementDetail agreement = converter.convertAgreementToDTO(ca);

    assertEquals(AGREEMENT_NUMBER, agreement.getNumber());
    assertEquals(AGREEMENT_NAME, agreement.getName());
    assertEquals(AGREEMENT_DESCRIPTION, agreement.getDescription());
    assertEquals(START_DATE, agreement.getStartDate());
    assertEquals(END_DATE, agreement.getEndDate());
    assertEquals(AGREEMENT_URL, agreement.getDetailUrl());

    LotSummary lotSummary = agreement.getLots().stream().findFirst().get();
    assertEquals(LOT_NAME, lotSummary.getName());
    assertEquals(LOT_NUMBER, lotSummary.getNumber());
  }

  @Test
  public void testLotDetailProduct() {
    LotDetail lotDetail = converter.convertLotToDTO(createTestLot("Products"));
    assertEquals(LOT_NAME, lotDetail.getName());
    assertEquals(LOT_NUMBER, lotDetail.getNumber());
    assertEquals(LotType.PRODUCT, lotDetail.getType());
    assertEquals(LOT_DESCRIPTION, lotDetail.getDescription());

    String sector = lotDetail.getSectors().stream().findFirst().get();
    assertEquals(SECTOR_NAME, sector);

    RouteToMarketDTO rtm = lotDetail.getRoutesToMarket().stream().findFirst().get();
    assertEquals(LOCATION, rtm.getLocation());
  }

  @Test
  public void testLotDetailService() {
    LotDetail lotDetail = converter.convertLotToDTO(createTestLot("Services"));
    assertEquals(LotType.SERVICE, lotDetail.getType());
  }

  @Test
  public void testLotDetailProductAndService() {
    LotDetail lotDetail = converter.convertLotToDTO(createTestLot("Products and Services"));
    assertEquals(LotType.PRODUCT_AND_SERVICE, lotDetail.getType());
  }

  private Lot createTestLot(String type) {

    Set<Sector> sectors = new HashSet<>();
    sectors.add(createSector());
    Set<LotRouteToMarket> lrtms = new HashSet<>();
    lrtms.add(createLotRouteToMarket());

    Lot lot = new Lot();
    lot.setNumber(LOT_NUMBER);
    lot.setName(LOT_NAME);
    lot.setDescription(LOT_DESCRIPTION);
    lot.setStartDate(START_DATE);
    lot.setEndDate(END_DATE);
    lot.setLotType(type);
    lot.setSectors(sectors);
    lot.setRoutesToMarket(lrtms);
    return lot;
  }

  private Sector createSector() {
    Sector sector = new Sector();
    sector.setName(SECTOR_NAME);
    return sector;
  }

  private RouteToMarket createRouteToMarket() {
    RouteToMarket rtm = new RouteToMarket();
    rtm.setName(ROUTE_TO_MARKET_NAME);
    rtm.setDescription(ROUTE_TO_MARKET_DESCRIPTION);
    return rtm;
  }

  private LotRouteToMarket createLotRouteToMarket() {
    LotRouteToMarketKey key = new LotRouteToMarketKey();
    key.setLotId(1);
    key.setRouteToMarketName(ROUTE_TO_MARKET_NAME);

    LotRouteToMarket lrtm = new LotRouteToMarket();
    lrtm.setRouteToMarket(createRouteToMarket());
    lrtm.setKey(key);

    lrtm.setLocation(LOCATION);
    return lrtm;
  }
}
