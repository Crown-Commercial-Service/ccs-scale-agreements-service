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
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

@SpringBootTest(classes = {AgreementConverter.class, ModelMapper.class, LotTypeConverter.class,
    DataTypeConverter.class, EvaluationTypeConverter.class, RelatedLotConverter.class,
    SectorConverter.class, AgreementUpdateConverter.class, RouteToMarketConverter.class,
    AgreementContactsConverter.class, LotSupplierPropertyMap.class, OrganisationConverter.class,
    LotContactsConverter.class, SupplierStatusConverter.class})
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

  private static final String ROUTE_TO_MARKET_NAME = "Direct Award";
  private static final String LRTM_BUYING_METHOD_URL = "http://buyingmethod";
  private static final Short LRTM_CONTRACT_LENGTH_MIN = 1;
  private static final Short LRTM_CONTRACT_LENGTH_MAX = 2;
  private static final String LRTM_CONTRACT_LENGHT_OUM = "years";
  private static final LocalDate LTRM_START_DATE = LocalDate.now();
  private static final LocalDate LTRM_END_DATE = LocalDate.now();
  private static final BigDecimal LRTM_MIN_VALUE = new BigDecimal(3);
  private static final BigDecimal LRTM_MAX_VALUE = new BigDecimal(4);
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
    ca.setOrganisationRoles(createCommercialAgreementOrgRoles());

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
        converter.convertAgreementUpdatesToDTOs(Arrays.asList(createCommercialAgreementUpdate()));
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
    assertEquals(BuyingMethod.DIRECT_AWARD, rtm.getBuyingMethod());
    assertEquals(LRTM_BUYING_METHOD_URL, rtm.getBuyingSystemUrl());
    assertEquals(LRTM_MIN_VALUE, rtm.getMinimumValue());
    assertEquals(LRTM_MAX_VALUE, rtm.getMaximumValue());
    assertEquals(LRTM_CONTRACT_LENGTH_MIN, rtm.getMinContractLength().getLength());
    assertEquals(LRTM_CONTRACT_LENGHT_OUM, rtm.getMinContractLength().getUnit());
    assertEquals(LRTM_CONTRACT_LENGTH_MAX, rtm.getMaxContractLength().getLength());
    assertEquals(LRTM_CONTRACT_LENGHT_OUM, rtm.getMaxContractLength().getUnit());

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
    lrtm.setBuyingMethodUrl(LRTM_BUYING_METHOD_URL);
    lrtm.setContractLengthMaximumValue(LRTM_CONTRACT_LENGTH_MAX);
    lrtm.setContractLengthMinimumValue(LRTM_CONTRACT_LENGTH_MIN);
    lrtm.setContractLengthUnitOfMeasure(LRTM_CONTRACT_LENGHT_OUM);
    lrtm.setEndDate(LTRM_END_DATE);
    lrtm.setStartDate(LTRM_START_DATE);
    lrtm.setMaximumValue(LRTM_MAX_VALUE);
    lrtm.setMinimumValue(LRTM_MIN_VALUE);
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

  private Set<CommercialAgreementOrgRole> createCommercialAgreementOrgRoles() {
    Set<ContactPointCommercialAgreementOrgRole> cpCaOrgRoles1 = new HashSet<>();
    Set<ContactPointCommercialAgreementOrgRole> cpCaOrgRoles2 = new HashSet<>();

    ContactPointCommercialAgreementOrgRole cpCaOrgRole1 =
        new ContactPointCommercialAgreementOrgRole();
    ContactDetail contactDetail1 = new ContactDetail();
    contactDetail1.setEmailAddress("cd1@example.com");
    contactDetail1.setTelephoneNumber("01234000001");
    contactDetail1.setFaxNumber("01234000001F");
    ContactPointReason cpReason1 = new ContactPointReason();
    cpReason1.setName("CP CA Reason 1");
    cpCaOrgRole1.setContactPointName("CP CA Org Role 1");
    cpCaOrgRole1.setContactPointReason(cpReason1);
    cpCaOrgRole1.setContactDetail(contactDetail1);

    ContactPointCommercialAgreementOrgRole cpCaOrgRole2 =
        new ContactPointCommercialAgreementOrgRole();
    ContactDetail contactDetail2 = new ContactDetail();
    contactDetail2.setEmailAddress("cd2@example.com");
    contactDetail2.setTelephoneNumber("01234000002");
    contactDetail2.setFaxNumber("01234000002F");
    ContactPointReason cpReason2 = new ContactPointReason();
    cpReason1.setName("CP CA Reason 1");
    cpCaOrgRole2.setContactPointName("CP CA Org Role 2");
    cpCaOrgRole2.setContactPointReason(cpReason2);
    cpCaOrgRole2.setContactDetail(contactDetail2);

    ContactPointCommercialAgreementOrgRole cpCaOrgRole3 =
        new ContactPointCommercialAgreementOrgRole();
    ContactDetail contactDetail3 = new ContactDetail();
    contactDetail3.setEmailAddress("cd3@example.com");
    contactDetail3.setTelephoneNumber("01234000003");
    contactDetail3.setFaxNumber("01234000003F");
    ContactPointReason cpReason3 = new ContactPointReason();
    cpReason1.setName("CP CA Reason 3");
    cpCaOrgRole3.setContactPointName("CP CA Org Role 3");
    cpCaOrgRole3.setContactPointReason(cpReason3);
    cpCaOrgRole3.setContactDetail(contactDetail3);

    cpCaOrgRoles1.add(cpCaOrgRole1);
    cpCaOrgRoles1.add(cpCaOrgRole2);
    cpCaOrgRoles2.add(cpCaOrgRole3);

    CommercialAgreementOrgRole caOrgRole1 = new CommercialAgreementOrgRole();
    CommercialAgreementOrgRole caOrgRole2 = new CommercialAgreementOrgRole();
    caOrgRole1.setContactPointCommercialAgreementOrgRole(cpCaOrgRoles1);
    caOrgRole2.setContactPointCommercialAgreementOrgRole(cpCaOrgRoles2);

    Set<CommercialAgreementOrgRole> commercialAgreementOrgRoles = new HashSet<>();
    commercialAgreementOrgRoles.add(caOrgRole1);
    commercialAgreementOrgRoles.add(caOrgRole2);

    return commercialAgreementOrgRoles;

  }
}
