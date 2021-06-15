package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

@WebMvcTest(AgreementController.class)
@Import(GlobalErrorHandler.class)
class AgreementControllerTest {

  private static final String GET_AGREEMENT_PATH = "/agreements/%s";
  private static final String GET_LOT_PATH = "/agreements/%s/lots/%s";
  private static final String GET_AGREEMENT_LOTS_PATH = "/agreements/%s/lots";
  private static final String GET_AGREEMENT_DOCUMENTS_PATH = "/agreements/%s/documents";
  private static final String GET_AGREEMENT_UPDATES_PATH = "/agreements/%s/updates";
  private static final String GET_LOT_SUPPLIERS_PATH =
      "/agreements/{ca-number}/lots/{lot-number}/suppliers";
  private static final String BUYING_METHOD_PARAM = "buyingMethod";
  private static final String BUYING_METHOD_VALUE = "EAuction";

  private static final String AGREEMENT_NUMBER = "RM3733";
  private static final String LOT1_NUMBER = "Lot 1";
  private static final String LOT2_NUMBER = "Lot 2";
  private static final String AGREEMENT_UPDATE_TEXT = "Update Text";

  private static final String DOCUMENT_NAME = "My Doc";
  private static final String DOCUMENT_URL = "http://document";
  private static final String DOCUMENT_TYPE = "tenderNotice";
  private static final String DOCUMENT_FORMAT = "text/html";
  private static final String DOCUMENT_LANGUAGE = "en_GB";
  private static final String DOCUMENT_DESCRIPTION = "Document Description";
  private static final Instant DOCUMENT_PUBLISHED_DATE = Instant.now();
  private static final Instant DOCUMENT_MODIFIED_DATE = Instant.now();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AgreementService service;

  @MockBean
  private AgreementConverter converter;

  @MockBean
  private CommercialAgreement mockCommercialAgreement;

  @MockBean
  private Lot mockLot;

  @MockBean
  private CommercialAgreementDocument mockCommercialAgreementDocument;

  @MockBean
  private CommercialAgreementUpdate mockCommercialAgreementUpdate;

  @MockBean
  private LotOrganisationRole lotOrganisationRole;

  @Test
  void testGetAgreementSummariesSuccess() throws Exception {
    final AgreementSummary agreement = new AgreementSummary();
    agreement.setNumber(AGREEMENT_NUMBER);

    when(converter.convertAgreementToSummaryDTO(mockCommercialAgreement)).thenReturn(agreement);
    when(service.getAgreements()).thenReturn(Arrays.asList(mockCommercialAgreement));
    mockMvc.perform(get("/agreements")).andExpect(status().isOk())
        .andExpect(jsonPath("$[0].number", is(AGREEMENT_NUMBER)));
  }

  @Test
  void testGetAgreementSuccess() throws Exception {
    final AgreementDetail agreement = new AgreementDetail();
    agreement.setNumber(AGREEMENT_NUMBER);

    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(converter.convertAgreementToDTO(mockCommercialAgreement)).thenReturn(agreement);
    mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER)).andExpect(status().isOk())
        .andExpect(jsonPath("$.number", is(AGREEMENT_NUMBER)));
  }

  @Test
  void testGetAgreementNotFound() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(null);
    mockMvc.perform(get(String.format(GET_AGREEMENT_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(AgreementNotFoundException.ERROR_MSG_TEMPLATE, AGREEMENT_NUMBER))));
  }

  @Test
  void testGetAgreementUnexpectedError() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
    mockMvc.perform(get(String.format(GET_AGREEMENT_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.INTERNAL_SERVER_ERROR.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_DEFAULT)));
  }

  @Test
  void testGetAgreementLotsSuccess() throws Exception {
    final LotDetail lot = new LotDetail();
    lot.setNumber(LOT1_NUMBER);
    final Set<Lot> mockLots = new HashSet<>(Arrays.asList(mockLot));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getLots()).thenReturn(mockLots);
    when(converter.convertLotsToDTOs(mockLots)).thenReturn(Arrays.asList(lot));
    mockMvc.perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().isOk()).andExpect(jsonPath("$[0].number", is(LOT1_NUMBER)));
  }

  @Test
  void testGetAgreementLotsFilteredByBuyingMethodSuccess() throws Exception {
    final LotDetail lot1 = new LotDetail();
    final RouteToMarketDTO directAward = new RouteToMarketDTO();
    directAward.setBuyingMethod(BuyingMethod.DIRECT_AWARD);
    lot1.setNumber(LOT1_NUMBER);
    lot1.setRoutesToMarket(Arrays.asList(directAward));

    final LotDetail lot2 = new LotDetail();
    final RouteToMarketDTO furtherCompetition = new RouteToMarketDTO();
    final RouteToMarketDTO eAuction = new RouteToMarketDTO();
    furtherCompetition.setBuyingMethod(BuyingMethod.E_AUCTION);
    lot2.setNumber(LOT2_NUMBER);
    lot2.setRoutesToMarket(Arrays.asList(furtherCompetition, eAuction));

    final Set<Lot> mockLots = new HashSet<>(Arrays.asList(mockLot));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getLots()).thenReturn(mockLots);
    when(converter.convertLotsToDTOs(mockLots)).thenReturn(Arrays.asList(lot1, lot2));
    mockMvc
        .perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER))
            .queryParam(BUYING_METHOD_PARAM, BUYING_METHOD_VALUE))
        .andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(1)))
        .andExpect(jsonPath("$[0].number", is(LOT2_NUMBER)));
  }

  @Test
  void testGetAgreementLotsFilteredByBuyingMethodBadRequest() throws Exception {
    mockMvc
        .perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER))
            .queryParam(BUYING_METHOD_PARAM, "InvalidBuyingMethod"))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.size()", is(1)))
        .andExpect(jsonPath("$.errors[0].status", is("400 BAD_REQUEST")))
        .andExpect(jsonPath("$.errors[0].title", is("Validation error processing the request")))
        .andExpect(jsonPath("$.errors[0].detail", is(
            "Buying method value invalid. Valid values are: [DirectAward, FurtherCompetition, Marketplace, EAuction]")));
  }

  @Test
  void testGetAgreementLotsNotFound() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(null);
    mockMvc.perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(AgreementNotFoundException.ERROR_MSG_TEMPLATE, AGREEMENT_NUMBER))));
  }

  @Test
  void testGetAgreementLotsUnexpectedError() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
    mockMvc.perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.INTERNAL_SERVER_ERROR.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_DEFAULT)));
  }

  @Test
  void testGetLotSuccess() throws Exception {
    final LotDetail lot = new LotDetail();
    lot.setNumber(LOT1_NUMBER);

    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(mockLot);
    when(converter.convertLotToDTO(mockLot)).thenReturn(lot);
    mockMvc.perform(get(String.format(GET_LOT_PATH, AGREEMENT_NUMBER, LOT1_NUMBER)))
        .andExpect(status().isOk()).andExpect(jsonPath("$.number", is(LOT1_NUMBER)));
  }

  @Test
  void testGetLotNotFound() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(null);
    mockMvc.perform(get(String.format(GET_LOT_PATH, AGREEMENT_NUMBER, LOT1_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND)))
        .andExpect(jsonPath("$.errors[0].detail", is(String
            .format(LotNotFoundException.ERROR_MSG_TEMPLATE, LOT1_NUMBER, AGREEMENT_NUMBER))));
  }

  @Test
  void testGetLotUnexpectedError() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
    mockMvc.perform(get(String.format(GET_LOT_PATH, AGREEMENT_NUMBER, LOT1_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.INTERNAL_SERVER_ERROR.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_DEFAULT)));
  }

  @Test
  void testGetAgreementDocuments() throws Exception {
    final Document document = new Document();
    document.setName(DOCUMENT_NAME);
    document.setDescription(DOCUMENT_DESCRIPTION);
    document.setUrl(DOCUMENT_URL);
    document.setDocumentType(DOCUMENT_TYPE);
    document.setFormat(DOCUMENT_FORMAT);
    document.setLanguage(DOCUMENT_LANGUAGE);
    document.setPublishedDate(DOCUMENT_PUBLISHED_DATE);
    document.setModifiedDate(DOCUMENT_MODIFIED_DATE);

    final Set<CommercialAgreementDocument> mockDocs =
        new HashSet<>(Arrays.asList(mockCommercialAgreementDocument));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getDocuments()).thenReturn(mockDocs);
    when(converter.convertAgreementDocumentsToDTOs(mockDocs)).thenReturn(Arrays.asList(document));
    mockMvc.perform(get(String.format(GET_AGREEMENT_DOCUMENTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().isOk()).andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title", is(DOCUMENT_NAME)))
        .andExpect(jsonPath("$[0].url", is(DOCUMENT_URL)))
        .andExpect(jsonPath("$[0].description", is(DOCUMENT_DESCRIPTION)))
        .andExpect(jsonPath("$[0].documentType", is(DOCUMENT_TYPE)))
        .andExpect(jsonPath("$[0].format", is(DOCUMENT_FORMAT)))
        .andExpect(jsonPath("$[0].language", is(DOCUMENT_LANGUAGE)))
        .andExpect(jsonPath("$[0].datePublished", is(DOCUMENT_PUBLISHED_DATE.toString())))
        .andExpect(jsonPath("$[0].dateModified", is(DOCUMENT_MODIFIED_DATE.toString())));
  }

  @Test
  void testGetAgreementDocumentsNotFound() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(null);
    mockMvc.perform(get(String.format(GET_AGREEMENT_DOCUMENTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(AgreementNotFoundException.ERROR_MSG_TEMPLATE, AGREEMENT_NUMBER))));
  }

  @Test
  void testGetAgreementDocumentsUnexpectedError() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
    mockMvc.perform(get(String.format(GET_AGREEMENT_DOCUMENTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.INTERNAL_SERVER_ERROR.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_DEFAULT)));
  }

  @Test
  void testGetAgreementUpdates() throws Exception {
    final AgreementUpdate update = new AgreementUpdate();
    update.setText(AGREEMENT_UPDATE_TEXT);
    final Set<CommercialAgreementUpdate> mockUpdates =
        new HashSet<>(Arrays.asList(mockCommercialAgreementUpdate));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getUpdates()).thenReturn(mockUpdates);
    when(converter.convertAgreementUpdatesToDTOs(mockUpdates)).thenReturn(Arrays.asList(update));
    mockMvc.perform(get(String.format(GET_AGREEMENT_UPDATES_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().isOk()).andExpect(jsonPath("$[0].text", is(AGREEMENT_UPDATE_TEXT)));
  }

  @Test
  void testGetAgreementUpdatesNotFound() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(null);
    mockMvc.perform(get(String.format(GET_AGREEMENT_UPDATES_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(AgreementNotFoundException.ERROR_MSG_TEMPLATE, AGREEMENT_NUMBER))));
  }

  @Test
  void testGetAgreementUpdatesUnexpectedError() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
    mockMvc.perform(get(String.format(GET_AGREEMENT_UPDATES_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.INTERNAL_SERVER_ERROR.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_DEFAULT)));
  }

  @Test
  void testGetLotSuppliers() throws Exception {
    final Set<LotOrganisationRole> lotOrgRoles = Collections.singleton(lotOrganisationRole);

    final Organization org = new Organization();
    org.setName("ABC Ltd");
    final ContactPoint contactPoint = new ContactPoint();
    contactPoint.setName("Procurement");
    final Contact contact = new Contact();
    contact.setContactId("abc123");
    contact.setContactPoint(contactPoint);
    final LotSupplier lotSupplier = new LotSupplier();
    lotSupplier.setOrganization(org);
    lotSupplier.setSupplierStatus(SupplierStatus.ACTIVE);
    lotSupplier.setLotContacts(Collections.singleton(contact));

    when(
        service.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
            .thenReturn(lotOrgRoles);
    when(converter.convertLotOrgRolesToLotSupplierDTOs(lotOrgRoles))
        .thenReturn(Collections.singleton(lotSupplier));

    mockMvc.perform(get(GET_LOT_SUPPLIERS_PATH, AGREEMENT_NUMBER, LOT1_NUMBER))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.size()", is(1)))
        .andExpect(jsonPath("$.[0].organization.name", is("ABC Ltd")))
        .andExpect(jsonPath("$.[0].supplierStatus", is("active")))
        .andExpect(jsonPath("$.[0].lotContacts.size()", is(1)))
        .andExpect(jsonPath("$.[0].lotContacts.[0].contactId", is("abc123")))
        .andExpect(jsonPath("$.[0].lotContacts.[0].contact.name", is("Procurement")));
  }

  @Test
  void testGetLotSuppliersNotFound() throws Exception {
    when(
        service.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
            .thenThrow(new LotNotFoundException(LOT1_NUMBER, AGREEMENT_NUMBER));

    mockMvc.perform(get(GET_LOT_SUPPLIERS_PATH, AGREEMENT_NUMBER, LOT1_NUMBER))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND)))
        .andExpect(jsonPath("$.errors[0].detail", is(String
            .format(LotNotFoundException.ERROR_MSG_TEMPLATE, LOT1_NUMBER, AGREEMENT_NUMBER))));
  }
}
