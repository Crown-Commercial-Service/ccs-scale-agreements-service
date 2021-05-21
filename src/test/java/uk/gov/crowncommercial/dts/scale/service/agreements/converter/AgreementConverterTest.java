package uk.gov.crowncommercial.dts.scale.service.agreements.converter;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Address;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.BuyingMethod;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Contact;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ContactPoint;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotRuleDTO;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotSupplier;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.NameValueType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Organization;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.OrganizationDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.OrganizationIdentifier;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.PartyRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RelatedAgreementLot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.RouteToMarketDTO;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Scheme;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatus;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.TransactionData;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementDocument;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointCommercialAgreementOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointReason;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRelatedLot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarketKey;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRule;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRuleAttribute;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRuleTransactionObject;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.RouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

@SpringBootTest(classes = {AgreementConverter.class, ModelMapper.class, LotTypeConverter.class,
    DataTypeConverter.class, EvaluationTypeConverter.class, RelatedLotConverter.class,
    SectorConverter.class, AgreementUpdateConverter.class, RouteToMarketConverter.class,
    AgreementContactsConverter.class, LotSupplierPropertyMap.class, OrganisationConverter.class,
    LotContactsConverter.class, SupplierStatusConverter.class, TimestampConverter.class,
    AgreementOwnerConverter.class})
@ActiveProfiles("test")
class AgreementConverterTest {

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
  private static final String DOCUMENT_FORMAT = "text/html";
  private static final String DOCUMENT_LANGUAGE = "en_GB";
  private static final String DOCUMENT_DESCRIPTION = "Document Description";
  private static final Integer DOCUMENT_VERSION = 1;
  private static final Timestamp DOCUMENT_PUBLISHED_DATE =
      new Timestamp(System.currentTimeMillis());
  private static final Timestamp DOCUMENT_MODIFIED_DATE = new Timestamp(System.currentTimeMillis());

  // Contact detail value templates
  private static final String CONTACT_EMAIL = "cd%d@example.com";
  private static final String CONTACT_PHONE = "0123400000%d";
  private static final String CONTACT_FAX = "0123400000%dF";
  private static final String CONTACT_NAME = "Contact %d";
  private static final String CONTACT_REASON = "Contact reason %d";
  private static final String CONTACT_STREET_ADDRESS = "%d Exeter Road";
  private static final String CONTACT_LOCALITY = "East Mid Devonsh%dre";
  private static final String CONTACT_REGION = "Devonsh%dre";
  private static final String CONTACT_POSTCODE = "EX%d0AB";
  private static final String CONTACT_COUNTRY_CODE = "GB%d";
  private static final String CONTACT_URL = "https://procurement.acmetrading%d.com";

  // Organisation value templates
  private static final String ORG_ENTITY_ID = "entity-id-%d";
  private static final String ORG_LEGAL_NAME = "ACME Trading Ltd %d";
  private static final String ORG_URI = "https://www.acmetrading%d.com";
  private static final String ORG_COMPANY_TYPE = "Small%d";

  @Autowired
  AgreementConverter converter;

  @MockBean
  private AgreementService service;

  @Test
  void testAgreementDetail() {

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
    ca.setOrganisationRoles(createCommercialAgreementOrgRoles(3));

    AgreementDetail agreement = converter.convertAgreementToDTO(ca);

    assertEquals(AGREEMENT_NUMBER, agreement.getNumber());
    assertEquals(AGREEMENT_NAME, agreement.getName());
    assertEquals(AGREEMENT_DESCRIPTION, agreement.getDescription());
    assertEquals(START_DATE, agreement.getStartDate());
    assertEquals(END_DATE, agreement.getEndDate());
    assertEquals(AGREEMENT_URL, agreement.getDetailUrl());
    assertEquals(3, agreement.getContacts().size());
    assertThat(agreement.getContacts(), hasItems(getContact(1), getContact(2), getContact(3)));

    LotSummary lotSummary = agreement.getLots().stream().findFirst().get();
    assertEquals(LOT_NAME, lotSummary.getName());
    assertEquals(LOT_NUMBER, lotSummary.getNumber());
  }

  @Test
  void testLotDetailProduct() {
    LotDetail lotDetail = converter.convertLotToDTO(createTestLot("Products"));
    testLot(lotDetail);
  }

  @Test
  void testLotDetailService() {
    LotDetail lotDetail = converter.convertLotToDTO(createTestLot("Services"));
    assertEquals(LotType.SERVICE, lotDetail.getType());
  }

  @Test
  void testLotDetailProductAndService() {
    LotDetail lotDetail = converter.convertLotToDTO(createTestLot("Products and Services"));
    assertEquals(LotType.PRODUCT_AND_SERVICE, lotDetail.getType());
  }

  @Test
  void testLotDetailProductCollection() {
    Collection<LotDetail> lots =
        converter.convertLotsToDTOs(Arrays.asList(createTestLot("Products")));
    testLot(lots.stream().findFirst().get());
  }

  @Test
  void testAgreementUpdateCollection() {
    Collection<AgreementUpdate> updates =
        converter.convertAgreementUpdatesToDTOs(Arrays.asList(createCommercialAgreementUpdate()));
    AgreementUpdate update = updates.stream().findFirst().get();
    assertEquals(UPDATE_NAME, update.getText());
    assertEquals(UPDATE_PUBLISHED_DATE, update.getDate());
    assertEquals(UPDATE_URL, update.getLinkUrl());
  }

  @Test
  void testAgreementDocumentCollection() {
    Collection<Document> documents = converter
        .convertAgreementDocumentsToDTOs(Arrays.asList(createCommercialAgreementDocument()));
    Document document = documents.stream().findFirst().get();
    assertEquals(DOCUMENT_TYPE, document.getDocumentType());
    assertEquals(DOCUMENT_NAME, document.getName());
    assertEquals(DOCUMENT_DESCRIPTION, document.getDescription());
    assertEquals(DOCUMENT_URL, document.getUrl());
    assertEquals(DOCUMENT_FORMAT, document.getFormat());
    assertEquals(DOCUMENT_LANGUAGE, document.getLanguage());
    assertEquals(DOCUMENT_PUBLISHED_DATE.toInstant(), document.getPublishedDate());
    assertEquals(DOCUMENT_MODIFIED_DATE.toInstant(), document.getModifiedDate());
  }

  @Test
  void testLotSuppliers() {
    Collection<LotSupplier> lotSuppliers =
        converter.convertLotOrgRolesToLotSupplierDTOs(createLotOrganisationRoles(3));

    assertEquals(2, lotSuppliers.size());

    // One LotSupplier should have 2 contacts, the other just 1. First in each is primary.
    LotSupplier lotSupplier1 = getLotSupplier(1, 1, 2);
    LotSupplier lotSupplier2 = getLotSupplier(2, 3, 3);

    assertThat(lotSuppliers, hasItems(lotSupplier1, lotSupplier2));
  }

  private LotSupplier getLotSupplier(int instance, int minContactPoint, int maxContactPoint) {
    LotSupplier lotSupplier = new LotSupplier();

    Organization org = new Organization();
    org.setName(format(ORG_LEGAL_NAME, instance));
    org.setId(format(ORG_ENTITY_ID, instance));
    org.setRoles(Collections.singleton(PartyRole.SUPPLIER)); // TODO - check

    final OrganizationIdentifier orgIdentifier = new OrganizationIdentifier();
    orgIdentifier.setId(format(ORG_ENTITY_ID, instance));
    orgIdentifier.setLegalName(format(ORG_LEGAL_NAME, instance));
    orgIdentifier.setUri(format(ORG_URI, instance));
    orgIdentifier.setScheme(Scheme.GB_COH);
    org.setIdentifier(orgIdentifier);

    OrganizationDetail orgDetail = new OrganizationDetail();
    orgDetail.setCreationDate(LocalDate.of(instance, 1, 1));
    orgDetail.setCountryCode("GB");
    orgDetail.setCompanyType(format(ORG_COMPANY_TYPE, instance));
    orgDetail.setIsVcse(true);
    orgDetail.setStatus("active");
    orgDetail.setActive(true);
    org.setDetails(orgDetail);

    // Primary Contact point + address
    Address address = new Address();
    address.setStreetAddress(format(CONTACT_STREET_ADDRESS, minContactPoint));
    address.setLocality(format(CONTACT_LOCALITY, minContactPoint));
    address.setRegion(format(CONTACT_REGION, minContactPoint));
    address.setPostalCode(format(CONTACT_POSTCODE, minContactPoint));
    address.setCountryName(format(CONTACT_COUNTRY_CODE, minContactPoint));
    org.setAddress(address);
    org.setContactPoint(getContact(minContactPoint).getContactPoint());

    lotSupplier.setOrganization(org);
    lotSupplier.setSupplierStatus(SupplierStatus.ACTIVE);

    // All contacts
    Set<Contact> contacts = new HashSet<>();
    IntStream.rangeClosed(minContactPoint, maxContactPoint).forEachOrdered(i -> {
      contacts.add(getContact(i));
    });

    lotSupplier.setLotContacts(contacts);
    return lotSupplier;
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
    document.setFormat(DOCUMENT_FORMAT);
    document.setLanguage(DOCUMENT_LANGUAGE);
    document.setDescription(DOCUMENT_DESCRIPTION);
    document.setPublishedDate(DOCUMENT_PUBLISHED_DATE);
    document.setModifiedDate(DOCUMENT_MODIFIED_DATE);
    return document;
  }

  private Set<CommercialAgreementOrgRole> createCommercialAgreementOrgRoles(int contactPointCount) {
    assertThat("contactPointCount should >= 2", contactPointCount, greaterThanOrEqualTo(2));

    Set<ContactPointCommercialAgreementOrgRole> cpCaOrgRoles1 = new HashSet<>();
    Set<ContactPointCommercialAgreementOrgRole> cpCaOrgRoles2 = new HashSet<>();

    IntStream.rangeClosed(1, contactPointCount).forEachOrdered(i -> {
      ContactPointCommercialAgreementOrgRole cpCaOrgRole =
          new ContactPointCommercialAgreementOrgRole();

      ContactPointReason cpReason = new ContactPointReason();
      cpReason.setName(format(CONTACT_REASON, i));
      cpCaOrgRole.setContactPointName(format(CONTACT_NAME, i));
      cpCaOrgRole.setContactPointReason(cpReason);
      cpCaOrgRole.setContactDetail(createContactDetail(i, false));

      // Spread over 2 CP CA Org Role sets
      if (i <= contactPointCount - 1) {
        cpCaOrgRoles1.add(cpCaOrgRole);
      } else {
        cpCaOrgRoles2.add(cpCaOrgRole);
      }
    });

    CommercialAgreementOrgRole caOrgRole1 = new CommercialAgreementOrgRole();
    CommercialAgreementOrgRole caOrgRole2 = new CommercialAgreementOrgRole();
    caOrgRole1.setContactPointCommercialAgreementOrgRoles(cpCaOrgRoles1);
    caOrgRole2.setContactPointCommercialAgreementOrgRoles(cpCaOrgRoles2);

    Set<CommercialAgreementOrgRole> commercialAgreementOrgRoles = new HashSet<>();
    commercialAgreementOrgRoles.add(caOrgRole1);
    commercialAgreementOrgRoles.add(caOrgRole2);

    return commercialAgreementOrgRoles;
  }

  /*
   * Creates 2 LotOrganisationRoles, one with multiple contact points the other a single one.
   */
  private Set<LotOrganisationRole> createLotOrganisationRoles(int contactPointCount) {
    assertThat("contactPointCount should >= 2", contactPointCount, greaterThanOrEqualTo(2));

    Set<ContactPointLotOrgRole> cpLotOrgRoles1 = new HashSet<>();
    Set<ContactPointLotOrgRole> cpLOtOrgRoles2 = new HashSet<>();

    IntStream.rangeClosed(1, contactPointCount).forEachOrdered(i -> {
      ContactPointLotOrgRole cpLotOrgRole = new ContactPointLotOrgRole();

      ContactPointReason cpReason = new ContactPointReason();
      cpReason.setName(format(CONTACT_REASON, i));
      cpLotOrgRole.setContactPointName(format(CONTACT_NAME, i));
      cpLotOrgRole.setContactPointReason(cpReason);
      cpLotOrgRole.setContactDetail(createContactDetail(i, true));

      // Spread over 2 CP Lot Org Role sets and set first in each as 'primary'
      if (i <= contactPointCount - 1) {
        if (i == 1) {
          cpLotOrgRole.setPrimary(true);
        }
        cpLotOrgRoles1.add(cpLotOrgRole);
      } else {
        cpLotOrgRole.setPrimary(true);
        cpLOtOrgRoles2.add(cpLotOrgRole);
      }
    });

    LotOrganisationRole lotOrgRole1 = new LotOrganisationRole();
    LotOrganisationRole lotOrgRole2 = new LotOrganisationRole();
    lotOrgRole1.setContactPointLotOrgRoles(cpLotOrgRoles1);
    lotOrgRole1.setOrganisation(createOrganisation(1));
    lotOrgRole2.setContactPointLotOrgRoles(cpLOtOrgRoles2);
    lotOrgRole2.setOrganisation(createOrganisation(2));

    Set<LotOrganisationRole> lotOrgRoles = new HashSet<>();
    lotOrgRoles.add(lotOrgRole1);
    lotOrgRoles.add(lotOrgRole2);

    return lotOrgRoles;
  }

  private Organisation createOrganisation(int instance) {
    Organisation org = new Organisation();
    org.setEntityId(format(ORG_ENTITY_ID, instance));
    org.setLegalName(format(ORG_LEGAL_NAME, instance));
    org.setUri(format(ORG_URI, instance));
    org.setRegistryCode("GB-COH");

    org.setIncorporationDate(LocalDate.of(instance, 1, 1));
    org.setIncorporationCountry("GB");
    org.setBusinessType(format(ORG_COMPANY_TYPE, instance));
    org.setIsSme(true);
    org.setIsVcse(true);
    org.setStatus("active");
    org.setIsActive(true);
    return org;
  }

  private ContactDetail createContactDetail(int instance, boolean includeAddress) {
    ContactDetail contactDetail = new ContactDetail();
    contactDetail.setEmailAddress(format(CONTACT_EMAIL, instance));
    contactDetail.setTelephoneNumber(format(CONTACT_PHONE, instance));
    contactDetail.setFaxNumber(format(CONTACT_FAX, instance));
    contactDetail.setUrl(format(CONTACT_URL, instance));

    if (includeAddress) {
      contactDetail.setStreetAddress(format(CONTACT_STREET_ADDRESS, instance));
      contactDetail.setLocality(format(CONTACT_LOCALITY, instance));
      contactDetail.setRegion(format(CONTACT_REGION, instance));
      contactDetail.setPostalCode(format(CONTACT_POSTCODE, instance));
      contactDetail.setCountryCode(format(CONTACT_COUNTRY_CODE, instance));
    }

    return contactDetail;
  }

  private Contact getContact(int instance) {
    Contact contact = new Contact();
    contact.setContactReason(format(CONTACT_REASON, instance));

    ContactPoint contactPoint = new ContactPoint();
    contactPoint.setName(format(CONTACT_NAME, instance));
    contactPoint.setEmail(format(CONTACT_EMAIL, instance));
    contactPoint.setTelephone(format(CONTACT_PHONE, instance));
    contactPoint.setFaxNumber(format(CONTACT_FAX, instance));
    contactPoint.setUrl(format(CONTACT_URL, instance));
    contact.setContactPoint(contactPoint);

    return contact;
  }

}
