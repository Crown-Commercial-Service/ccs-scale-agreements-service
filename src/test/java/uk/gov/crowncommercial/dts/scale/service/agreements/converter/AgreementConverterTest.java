package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotRuleDTO;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.NameValueType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RelatedAgreementLot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RouteToMarketDTO;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.TransactionData;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementDocument;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRelatedLot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarketKey;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRule;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRuleAttribute;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRuleTransactionObject;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.RouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

@SpringBootTest(classes = {AgreementConverter.class, ModelMapper.class, LotTypeConverter.class,
    DataTypeConverter.class, EvaluationTypeConverter.class, RelatedLotConverter.class,
    SectorConverter.class, AgreementUpdateConverter.class})
@ActiveProfiles("test")
public class AgreementConverterTest {

  private static final LocalDate START_DATE = LocalDate.now();
  private static final LocalDate END_DATE = LocalDate.now();

  private static final String AGREEMENT_NUMBER = "R001";
  private static final String AGREEMENT_NAME = "Agreement 1";
  private static final String AGREEMENT_DESCRIPTION = "Agreement description";
  private static final String AGREEMENT_URL = "Agreement url";

  private static final Integer LOT_ID = 1;
  private static final String LOT_NUMBER = "Lot 1";
  private static final String LOT_NAME = "Test Lot";
  private static final String LOT_DESCRIPTION = "Test Description";

  private static final Integer LOT_RULE_ID = 99;
  private static final String LOT_RULE_NAME = "Lot Rule 1";
  private static final String LOT_RULE_SERVICE = "BAT";
  private static final String LOT_RULE_EVALUATION_TYPE = "flag";
  private static final String LOT_RULE_ATT_NAME = "Lot Rule Attribute";
  private static final String LOT_RULE_ATT_TYPE = "number";
  private static final String LOT_RULE_ATT_VALUE_TEXT = "Rule Value Text";
  private static final BigDecimal LOT_RULE_ATT_VALUE_NUMBER = new BigDecimal(123);

  private static final String LOT_RULE_TRANS_OBJECT_NAME = "Transaction Object";

  private static final String SECTOR_NAME = "Sector Name";

  private static final String ROUTE_TO_MARKET_NAME = "RTM Name";
  private static final String ROUTE_TO_MARKET_DESCRIPTION = "RTM Description";

  private static final String LOCATION = "Mordor";

  private static final Integer RELATED_LOT_ID = 888;
  private static final String RELATED_AGREEMENT_NUMBER = "RM666";
  private static final String RELATED_LOT_NUMBER = "Lot 4";
  private static final String RELATED_LOT_RELATIONSHIP = "FurtherCompetitionWhenBudgetExceeded";

  private static final String UPDATE_NAME = "Update Name";
  private static final LocalDate UPDATE_PUBLISHED_DATE = LocalDate.now();
  private static final Timestamp UPDATE_PUBLISHED_DATE_TS = Timestamp
      .from(Instant.from(UPDATE_PUBLISHED_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()));
  private static final String UPDATE_URL = "http://update";

  private static final String DOCUMENT_TYPE = "Document Type";
  private static final String DOCUMENT_NAME = "Document Name";
  private static final String DOCUMENT_URL = "http://document";
  private static final Integer DOCUMENT_VERSION = 1;

  @Autowired
  AgreementConverter converter;

  @MockBean
  private AgreementService service;

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
    testLot(lotDetail);
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

  @Test
  public void testLotDetailProductCollection() {
    Collection<LotDetail> lots =
        converter.convertLotsToDTOs(Arrays.asList(createTestLot("Products")));
    testLot(lots.stream().findFirst().get());
  }

  @Test
  public void testAgreementUpdateCollection() {
    Collection<AgreementUpdate> updates =
        converter.convertAgreementUpdatesToDTO(Arrays.asList(createCommercialAgreementUpdate()));
    AgreementUpdate update = updates.stream().findFirst().get();
    assertEquals(UPDATE_NAME, update.getText());
    assertEquals(UPDATE_PUBLISHED_DATE, update.getDate());
    assertEquals(UPDATE_URL, update.getLinkUrl());
  }

  @Test
  public void testAgreementDocumentCollection() {
    Collection<Document> documents = converter
        .convertAgreementDocumentsToDTOs(Arrays.asList(createCommercialAgreementDocument()));
    Document document = documents.stream().findFirst().get();
    assertEquals(DOCUMENT_TYPE, document.getDocumentType());
    assertEquals(DOCUMENT_NAME, document.getName());
    assertEquals(DOCUMENT_URL, document.getUrl());
    assertEquals(DOCUMENT_VERSION, document.getVersion());
  }

  private void testLot(LotDetail lotDetail) {
    assertEquals(LOT_NAME, lotDetail.getName());
    assertEquals(LOT_NUMBER, lotDetail.getNumber());
    assertEquals(LotType.PRODUCT, lotDetail.getType());
    assertEquals(LOT_DESCRIPTION, lotDetail.getDescription());
    assertEquals(START_DATE, lotDetail.getStartDate());
    assertEquals(END_DATE, lotDetail.getEndDate());

    String sector = lotDetail.getSectors().stream().findFirst().get();
    assertEquals(SECTOR_NAME, sector);

    RouteToMarketDTO rtm = lotDetail.getRoutesToMarket().stream().findFirst().get();
    assertEquals(LOCATION, rtm.getLocation());

    LotRuleDTO rule = lotDetail.getRules().stream().findFirst().get();
    assertEquals(LOT_RULE_ID.toString(), rule.getRuleId());
    assertEquals(LOT_RULE_NAME, rule.getName());
    assertEquals(LOT_RULE_SERVICE, rule.getService());
    assertEquals(LOT_RULE_EVALUATION_TYPE, rule.getEvaluationType().toString().toLowerCase());

    NameValueType att = rule.getLotAttributes().stream().findFirst().get();
    assertEquals(LOT_RULE_ID.toString(), rule.getRuleId());
    assertEquals(LOT_RULE_ATT_NAME, att.getName());
    assertEquals(LOT_RULE_ATT_TYPE, att.getDataType().toString().toLowerCase());
    assertEquals(LOT_RULE_ATT_VALUE_NUMBER, att.getValueNumber());
    assertEquals(LOT_RULE_ATT_VALUE_TEXT, att.getValueText());

    TransactionData object = rule.getTransactionData().stream().findFirst().get();
    assertEquals(LOT_RULE_TRANS_OBJECT_NAME, object.getName());
    assertEquals(LOCATION, object.getLocation());

    RelatedAgreementLot related = lotDetail.getRelatedAgreementLots().stream().findFirst().get();
    assertEquals(RELATED_AGREEMENT_NUMBER, related.getCaNumber());
    assertEquals(RELATED_LOT_NUMBER, related.getLotNumber());
    assertEquals(RELATED_LOT_RELATIONSHIP, related.getRelationship());
  }

  private Lot createTestLot(String type) {

    when(service.getLot(RELATED_LOT_ID)).thenReturn(createRelatedLot());

    Set<Sector> sectors = new HashSet<>();
    sectors.add(createSector());
    Set<LotRouteToMarket> lrtms = new HashSet<>();
    lrtms.add(createLotRouteToMarket());
    Set<LotRule> rules = new HashSet<>();
    rules.add(createLotRule());
    Set<LotRelatedLot> related = new HashSet<>();
    related.add(createLotRelatedLot());

    Lot lot = new Lot();
    lot.setId(LOT_ID);
    lot.setNumber(LOT_NUMBER);
    lot.setName(LOT_NAME);
    lot.setDescription(LOT_DESCRIPTION);
    lot.setStartDate(START_DATE);
    lot.setEndDate(END_DATE);
    lot.setLotType(type);
    lot.setSectors(sectors);
    lot.setRoutesToMarket(lrtms);
    lot.setRules(rules);
    lot.setRelatedAgreementLots(related);

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

  private LotRuleAttribute createLotRuleAttribute() {
    LotRuleAttribute att = new LotRuleAttribute();
    att.setName(LOT_RULE_ATT_NAME);
    att.setRuleId(LOT_RULE_ID);
    att.setDataType(LOT_RULE_ATT_TYPE);
    att.setValueNumber(LOT_RULE_ATT_VALUE_NUMBER);
    att.setValueText(LOT_RULE_ATT_VALUE_TEXT);
    return att;
  }

  private LotRuleTransactionObject createLotRuleTransactionObject() {
    LotRuleTransactionObject to = new LotRuleTransactionObject();
    to.setName(LOT_RULE_TRANS_OBJECT_NAME);
    to.setLocation(LOCATION);
    to.setRuleId(LOT_RULE_ID);
    return to;
  }

  private LotRule createLotRule() {
    LotRule rule = new LotRule();
    rule.setLotId(LOT_ID);
    rule.setName(LOT_RULE_NAME);
    rule.setRuleId(LOT_RULE_ID);
    rule.setService(LOT_RULE_SERVICE);
    rule.setEvaluationType(LOT_RULE_EVALUATION_TYPE);

    Set<LotRuleAttribute> attributes = new HashSet<>();
    attributes.add(createLotRuleAttribute());
    rule.setLotAttributes(attributes);

    Set<LotRuleTransactionObject> objects = new HashSet<>();
    objects.add(createLotRuleTransactionObject());
    rule.setTransactionData(objects);

    return rule;
  }

  private LotRelatedLot createLotRelatedLot() {
    CommercialAgreement ca = new CommercialAgreement();
    ca.setNumber(RELATED_AGREEMENT_NUMBER);

    LotRule rule = new LotRule();
    rule.setLotId(RELATED_LOT_ID);
    Set<LotRule> rules = new HashSet<>();
    rules.add(rule);

    Lot lot = new Lot();
    lot.setNumber(RELATED_LOT_NUMBER);
    lot.setAgreement(ca);
    lot.setRules(rules);

    LotRelatedLot related = new LotRelatedLot();
    related.setId(321);
    related.setRelationship(RELATED_LOT_RELATIONSHIP);
    related.setRule(rule);

    return related;
  }

  private Lot createRelatedLot() {
    CommercialAgreement relatedAgreement = new CommercialAgreement();
    relatedAgreement.setNumber(RELATED_AGREEMENT_NUMBER);

    Lot relatedLot = new Lot();
    relatedLot.setNumber(RELATED_LOT_NUMBER);
    relatedLot.setAgreement(relatedAgreement);

    return relatedLot;
  }

  private CommercialAgreementUpdate createCommercialAgreementUpdate() {
    CommercialAgreementUpdate update = new CommercialAgreementUpdate();
    update.setName(UPDATE_NAME);
    update.setPublishedDate(UPDATE_PUBLISHED_DATE_TS);
    update.setUrl(UPDATE_URL);
    return update;
  }

  private CommercialAgreementDocument createCommercialAgreementDocument() {
    CommercialAgreementDocument document = new CommercialAgreementDocument();
    document.setDocumentType(DOCUMENT_TYPE);
    document.setName(DOCUMENT_NAME);
    document.setUrl(DOCUMENT_URL);
    document.setVersion(DOCUMENT_VERSION);
    return document;
  }
}
