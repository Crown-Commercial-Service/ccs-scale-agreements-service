package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.rollbar.notifier.Rollbar;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidAgreementException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidLotException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidProcurementQuestionTemplateException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

@WebMvcTest(AgreementController.class)
@ActiveProfiles("test")
@Import(GlobalErrorHandler.class)
class AgreementControllerTest {

  private static final String GET_AGREEMENT_PATH = "/agreements/%s";
  private static final String GET_AGREEMENT_LOTS_PATH = "/agreements/%s/lots";
  private static final String GET_AGREEMENT_DOCUMENTS_PATH = "/agreements/%s/documents";
  private static final String GET_AGREEMENT_UPDATES_PATH = "/agreements/%s/updates";
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
  private static final String RUNTIME_EXCEPTION_TEXT = "Something is amiss";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BusinessLogicClient businessLogicClient;

  @MockBean
  private AgreementService service;
  
  @Autowired
  private AgreementController controller;

  @MockBean
  private CommercialAgreement mockCommercialAgreement;

  @MockBean
  private Lot mockLot;
  
  @MockBean 
  private Rollbar rollbar;

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

    when(businessLogicClient.getAgreementsList()).thenReturn(Arrays.asList(agreement));
    mockMvc.perform(get("/agreements")).andExpect(status().isOk())
        .andExpect(jsonPath("$[0].number", is(AGREEMENT_NUMBER)));
  }

  @Test
  void testGetAgreementSuccess() throws Exception {
    final AgreementDetail agreement = new AgreementDetail();
    agreement.setNumber(AGREEMENT_NUMBER);

    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(businessLogicClient.getAgreementDetail(AGREEMENT_NUMBER)).thenReturn(agreement);
    mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER)).andExpect(status().isOk())
        .andExpect(jsonPath("$.number", is(AGREEMENT_NUMBER)));
  }

  @Test
  void testGetAgreementNotFound() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(null);

    when(businessLogicClient.getAgreementDetail(AGREEMENT_NUMBER)).thenThrow(new AgreementNotFoundException(AGREEMENT_NUMBER));

    mockMvc.perform(get(String.format(GET_AGREEMENT_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(AgreementNotFoundException.ERROR_MSG_TEMPLATE, AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testGetAgreementUnexpectedError() throws Exception {
    when(businessLogicClient.getAgreementDetail(AGREEMENT_NUMBER)).thenThrow(new RuntimeException(RUNTIME_EXCEPTION_TEXT));

    mockMvc.perform(get(String.format(GET_AGREEMENT_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_DEFAULT_DESCRIPTION)));
  }

  @Test
  void testGetAgreementLotsSuccess() throws Exception {
    final LotDetail lot = new LotDetail();
    lot.setNumber(LOT1_NUMBER);
    final Set<Lot> mockLots = new HashSet<>(Arrays.asList(mockLot));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getLots()).thenReturn(mockLots);
    when(businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.NONE)).thenReturn(Arrays.asList(lot));

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
    when(businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.E_AUCTION)).thenReturn(Arrays.asList(lot2));
    mockMvc
        .perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER))
            .queryParam(BUYING_METHOD_PARAM, BUYING_METHOD_VALUE))
        .andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(1)))
        .andExpect(jsonPath("$[0].number", is(LOT2_NUMBER)));
  }

  @Test
  void testGetAgreementLotsFilteredByBuyingMethodBadRequest() throws Exception {
    when(businessLogicClient.getLotsForAgreement(eq(AGREEMENT_NUMBER), any())).thenThrow(new IllegalArgumentException(String.format("Buying method value invalid. Valid values are: %s", BuyingMethod.names())));

    mockMvc
        .perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER))
            .queryParam(BUYING_METHOD_PARAM, "InvalidBuyingMethod"))
        .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.size()", is(1)))
        .andExpect(jsonPath("$.errors[0].status", is("400 BAD_REQUEST")))
        .andExpect(jsonPath("$.errors[0].title", is("Validation error processing the request")))
        .andExpect(jsonPath("$.errors[0].detail", is(
            "Buying method value invalid. Valid values are: [DirectAward, FurtherCompetition, Marketplace, EAuction, NotSpecified]")))
        .andExpect(
            jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION)));
  }

  @Test
  void testGetAgreementLotsNotFound() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(null);

    when(businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.NONE)).thenThrow(new AgreementNotFoundException(AGREEMENT_NUMBER));

    mockMvc.perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(AgreementNotFoundException.ERROR_MSG_TEMPLATE, AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testGetAgreementLotsUnexpectedError() throws Exception {
    when(businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.NONE)).thenThrow(new RuntimeException(RUNTIME_EXCEPTION_TEXT));

    mockMvc.perform(get(String.format(GET_AGREEMENT_LOTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_DEFAULT_DESCRIPTION)));
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
    when(businessLogicClient.getDocumentsForAgreement(AGREEMENT_NUMBER)).thenReturn(Arrays.asList(document));
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

    when(businessLogicClient.getDocumentsForAgreement(AGREEMENT_NUMBER)).thenThrow(new AgreementNotFoundException(AGREEMENT_NUMBER));

    mockMvc.perform(get(String.format(GET_AGREEMENT_DOCUMENTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(AgreementNotFoundException.ERROR_MSG_TEMPLATE, AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testGetAgreementDocumentsUnexpectedError() throws Exception {
    when(businessLogicClient.getDocumentsForAgreement(AGREEMENT_NUMBER)).thenThrow(new RuntimeException(RUNTIME_EXCEPTION_TEXT));

    mockMvc.perform(get(String.format(GET_AGREEMENT_DOCUMENTS_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_DEFAULT_DESCRIPTION)));
  }

  @Test
  void testGetAgreementUpdates() throws Exception {
    final AgreementUpdate update = new AgreementUpdate();
    update.setText(AGREEMENT_UPDATE_TEXT);
    final Set<CommercialAgreementUpdate> mockUpdates =
        new HashSet<>(Arrays.asList(mockCommercialAgreementUpdate));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getUpdates()).thenReturn(mockUpdates);
    when(businessLogicClient.getUpdatesForAgreement(AGREEMENT_NUMBER)).thenReturn(Arrays.asList(update));
    mockMvc.perform(get(String.format(GET_AGREEMENT_UPDATES_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().isOk()).andExpect(jsonPath("$[0].text", is(AGREEMENT_UPDATE_TEXT)));
  }

  @Test
  void testGetAgreementUpdatesNotFound() throws Exception {
    when(businessLogicClient.getUpdatesForAgreement(AGREEMENT_NUMBER)).thenThrow(new AgreementNotFoundException(AGREEMENT_NUMBER));
    mockMvc.perform(get(String.format(GET_AGREEMENT_UPDATES_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(AgreementNotFoundException.ERROR_MSG_TEMPLATE, AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testGetAgreementUpdatesUnexpectedError() throws Exception {
    when(businessLogicClient.getUpdatesForAgreement(AGREEMENT_NUMBER)).thenThrow(new RuntimeException(RUNTIME_EXCEPTION_TEXT));

    mockMvc.perform(get(String.format(GET_AGREEMENT_UPDATES_PATH, AGREEMENT_NUMBER)))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_DEFAULT_DESCRIPTION)));
  }

  @Test
  void testCreateAgreementValid() throws Exception {

    String agreementNumber = "RM1045";
    AgreementDetail inputValue = new AgreementDetail("Technology Products 2", "Short textual description of the commercial agreement", "CCS", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    AgreementDetail result = new AgreementDetail("Technology Products 2", "Short textual description of the commercial agreement", "CCS", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    result.setNumber(agreementNumber);
    when(businessLogicClient.saveAgreement(inputValue, agreementNumber)).thenReturn(result);

    mockMvc.perform(MockMvcRequestBuilders
                    .put("/agreements/RM1045")
                    .content(asJsonString(inputValue))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("number", is(agreementNumber)))
            .andExpect(jsonPath("name", is(inputValue.getName())))
            .andExpect(jsonPath("description", is(inputValue.getDescription())))
            .andExpect(jsonPath("startDate", is(inputValue.getStartDate().toString())))
            .andExpect(jsonPath("preDefinedLotRequired", is(inputValue.getPreDefinedLotRequired())));
  }

  @Test
  void testCreateAgreementInValidEmptyAgreementName() throws Exception {

    String agreementNumber = "RM1045";
    AgreementDetail inputValue = new AgreementDetail("Technology Products 2", "Short textual description of the commercial agreement", "CCS", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(5), "URL", true);
    inputValue.setName(null);

    when(businessLogicClient.saveAgreement(inputValue, agreementNumber)).thenThrow(new InvalidAgreementException("name"));

    mockMvc.perform(MockMvcRequestBuilders
                    .put("/agreements/RM1045")
                    .content(asJsonString(inputValue))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION)))
            .andExpect(jsonPath("$.errors..detail", is(new ArrayList<String>(List.of(new String[]{"Invalid agreement format, missing 'name'"})))));
  }

  @Test
  void testSavingLotsWithAllValid() throws Exception {

    LotDetail lotDetail = new LotDetail("1", "Lot 1 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);
    LotDetail lotDetail1 = new LotDetail("2", "Lot 2 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.SERVICE);

    Collection<LotDetail> inputSet  = new HashSet<LotDetail>(List.of(lotDetail, lotDetail1));
    Collection<LotDetail> outputSet  = new HashSet<LotDetail>(List.of(lotDetail, lotDetail1));

    when(businessLogicClient.saveLots(inputSet,AGREEMENT_NUMBER)).thenReturn(outputSet);

    mockMvc.perform(MockMvcRequestBuilders
                    .put(String.format("/agreements/%s/lots", AGREEMENT_NUMBER))
                    .content(asJsonString(inputSet))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].number").isString());
  }


  @Test
  void testSavingLotsWithOneInvalid() throws Exception {

    LotDetail lotDetail = new LotDetail("1", "Lot 1 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);
    LotDetail lotDetail1 = new LotDetail("2", "Lot 2 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.SERVICE);
    lotDetail.setDescription(null);
    Collection<LotDetail> inputSet  = new HashSet<LotDetail>(List.of(lotDetail, lotDetail1));


    Mockito.doThrow(new InvalidLotException("description","1")).when(businessLogicClient).saveLots(inputSet,AGREEMENT_NUMBER);
    mockMvc.perform(MockMvcRequestBuilders
                    .put(String.format("/agreements/%s/lots", AGREEMENT_NUMBER))
                    .content(asJsonString(inputSet))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION)))
            .andExpect(jsonPath("$.errors..detail", is(new ArrayList<String>(List.of(new String[]{"Invalid lot format, missing 'description' for Lot 1"})))));
  }

  @Test
  void testUpdatingDataTemplate() throws Exception {
    // Mock Payload for Request
    ProcurementDataTemplate updatePayload = new ProcurementDataTemplate();
    updatePayload.setId(99);
    updatePayload.setTemplateName("FC2-DOS6-Lot3-TESTING91112121");
    updatePayload.setParent(37);
    updatePayload.setMandatory(true);
    updatePayload.setCreatedBy("agreement-service-testing");

    Map<String, Object> criterion = new HashMap<>();
    criterion.put("id", "Criterion 1");
    criterion.put("title", "About the procurement competitionax");
    criterion.put("source", "buyer");
    criterion.put("relatesTo", "buyer");
    criterion.put("description", "For Information Only");
    criterion.put("requirementGroups", new ArrayList<>());

    updatePayload.setCriteria(List.of(criterion));

    ProcurementQuestionTemplate savedTemplate = new ProcurementQuestionTemplate();
    savedTemplate.setId(updatePayload.getId());
    savedTemplate.setTemplateName(updatePayload.getTemplateName());
    savedTemplate.setParent(updatePayload.getParent());
    savedTemplate.setMandatory(updatePayload.getMandatory());
    savedTemplate.setCreatedBy(updatePayload.getCreatedBy());
    savedTemplate.setTemplatePayload(updatePayload.getCriteria());

    when(businessLogicClient.updateEventDataTemplates(any(ProcurementDataTemplate.class)))
            .thenReturn(updatePayload);

    // Perform Request
    mockMvc.perform(MockMvcRequestBuilders
                    .put("/data-templates")
                    .content(asJsonString(updatePayload))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(updatePayload.getId())))
            .andExpect(jsonPath("$.templateName", is(updatePayload.getTemplateName())))
            .andExpect(jsonPath("$.parent", is(updatePayload.getParent())))
            .andExpect(jsonPath("$.mandatory", is(updatePayload.getMandatory())))
            .andExpect(jsonPath("$.createdBy", is(updatePayload.getCreatedBy())))
            .andExpect(jsonPath("$.criteria[0].id", is("Criterion 1")))
            .andExpect(jsonPath("$.criteria[0].title", is("About the procurement competitionax")))
            .andExpect(jsonPath("$.criteria[0].source", is("buyer")))
            .andExpect(jsonPath("$.criteria[0].relatesTo", is("buyer")))
            .andExpect(jsonPath("$.criteria[0].description", is("For Information Only")));
  }

  public static String asJsonString(final Object obj) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {

      throw new RuntimeException(e);
    }
  }


}
