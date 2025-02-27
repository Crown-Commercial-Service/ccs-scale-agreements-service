package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rollbar.notifier.Rollbar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidLotException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidOrganisationException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.QuestionTemplateService;

@WebMvcTest(LotController.class)
@Import(GlobalErrorHandler.class)
class LotControllerTest {

  private static final String GET_LOT_PATH = "/agreements/{agreement-id}/lots/{lot-id}";
  private static final String GET_LOT_SUPPLIERS_PATH =
      "/agreements/{agreement-id}/lots/{lot-id}/suppliers";
  private static final String GET_LOT_EVENT_TYPES_PATH =
      "/agreements/{agreement-id}/lots/{lot-id}/event-types";
  private static final String GET_LOT_DATA_TEMPLATES_PATH =
      "/agreements/{agreement-id}/lots/{lot-id}/event-types/{event-type}/data-templates";
  private static final String GET_LOT_DOCUMENT_TEMPLATES_PATH =
      "/agreements/{agreement-id}/lots/{lot-id}/event-types/{event-type}/document-templates";

  private static final String AGREEMENT_NUMBER = "RM3733";
  private static final String LOT1_NUMBER = "Lot 1";
  private static final String EVENT_TYPE_RFI = "RFI";
  private static final String RUNTIME_EXCEPTION_TEXT = "Something is amiss";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AgreementService service;

  @MockBean
  private BusinessLogicClient businessLogicClient;

  @MockBean
  private QuestionTemplateService templateService;

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

  @MockBean
  private LotProcurementEventType lotProcurementEventType;

  @MockBean
  private Set<LotProcurementQuestionTemplate> lotProcurementQuestionTemplates;

  @MockBean
  private Rollbar rollbar;

  @Test
  void testGetLotSuccess() throws Exception {
    final var lot = new LotDetail();
    lot.setNumber(LOT1_NUMBER);

    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(mockLot);

    when(businessLogicClient.getLotDetail(AGREEMENT_NUMBER, LOT1_NUMBER)).thenReturn(lot);

    mockMvc.perform(get(GET_LOT_PATH, AGREEMENT_NUMBER, LOT1_NUMBER)).andExpect(status().isOk())
        .andExpect(jsonPath("$.number", is(LOT1_NUMBER)));
  }

  @Test
  void testGetLotNotFound() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(null);

    when(businessLogicClient.getLotDetail(AGREEMENT_NUMBER, LOT1_NUMBER)).thenThrow(new LotNotFoundException(LOT1_NUMBER, AGREEMENT_NUMBER));

    mockMvc.perform(get(GET_LOT_PATH, AGREEMENT_NUMBER, LOT1_NUMBER))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(LotNotFoundException.ERROR_MSG_TEMPLATE, LOT1_NUMBER,
                AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testGetLotUnexpectedError() throws Exception {
    when(businessLogicClient.getLotDetail(AGREEMENT_NUMBER, LOT1_NUMBER)).thenThrow(new RuntimeException(RUNTIME_EXCEPTION_TEXT));

    mockMvc.perform(get(GET_LOT_PATH, AGREEMENT_NUMBER, LOT1_NUMBER))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_DEFAULT_DESCRIPTION)));
  }

  @Test
  void testGetLotSuppliers() throws Exception {
    final Set<LotOrganisationRole> lotOrgRoles = Collections.singleton(lotOrganisationRole);

    final var org = new Organization();
    org.setName("ABC Ltd");
    final var contactPoint = new ContactPoint();
    contactPoint.setName("Procurement");
    final var contact = new Contact();
    contact.setContactId("abc123");
    contact.setContactPoint(contactPoint);
    final var lotSupplier = new LotSupplier();
    lotSupplier.setOrganization(org);
    lotSupplier.setSupplierStatus(SupplierStatus.ACTIVE);
    lotSupplier.setLotContacts(Collections.singleton(contact));

    when(
        service.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
            .thenReturn(lotOrgRoles);

    when(businessLogicClient.getLotSuppliers(AGREEMENT_NUMBER, LOT1_NUMBER)).thenReturn(Collections.singleton(lotSupplier));

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

    when(businessLogicClient.getLotSuppliers(AGREEMENT_NUMBER, LOT1_NUMBER)).thenThrow(new LotNotFoundException(LOT1_NUMBER, AGREEMENT_NUMBER));

    mockMvc.perform(get(GET_LOT_SUPPLIERS_PATH, AGREEMENT_NUMBER, LOT1_NUMBER))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(LotNotFoundException.ERROR_MSG_TEMPLATE, LOT1_NUMBER,
                AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testGetLotEventTypes() throws Exception {
    final Set<LotProcurementEventType> lotProcurementEventTypes =
        Collections.singleton(lotProcurementEventType);

    var eventType = new EventType();
    eventType.setType("RFI");
    eventType.setAssessmentToolId("FCA_TOOL_1");

    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(mockLot);
    when(mockLot.getProcurementEventTypes()).thenReturn(lotProcurementEventTypes);

    when(businessLogicClient.getLotEventTypes(AGREEMENT_NUMBER, LOT1_NUMBER)).thenReturn(Collections.singleton(eventType));

    mockMvc.perform(get(GET_LOT_EVENT_TYPES_PATH, AGREEMENT_NUMBER, LOT1_NUMBER))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.size()", is(1)))
        .andExpect(jsonPath("$.[0].type", is("RFI")))
        .andExpect(jsonPath("$.[0].assessmentToolId", is("FCA_TOOL_1")));
  }

  @Test
  void testGetLotEventTypesNotFound() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(null);

    when(businessLogicClient.getLotEventTypes(AGREEMENT_NUMBER, LOT1_NUMBER)).thenThrow(new LotNotFoundException(LOT1_NUMBER, AGREEMENT_NUMBER));

    mockMvc.perform(get(GET_LOT_EVENT_TYPES_PATH, AGREEMENT_NUMBER, LOT1_NUMBER))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(LotNotFoundException.ERROR_MSG_TEMPLATE, LOT1_NUMBER,
                AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testGetDataTemplates() throws Exception {
    final var expectedTemplatePayload = "[{\"id\\\": \\\"Criterion 1\\\"}]";
    ProcurementDataTemplate dataTemplate = new ProcurementDataTemplate();
    dataTemplate.setCriteria("[{\"id\\\": \\\"Criterion 1\\\"}]");
    when(mockLot.getProcurementQuestionTemplates()).thenReturn(lotProcurementQuestionTemplates);
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(mockLot);
    when(templateService.getDataTemplates(mockLot, EVENT_TYPE_RFI))
            .thenReturn(Collections.singleton(dataTemplate));

    when(businessLogicClient.getEventDataTemplates(AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI)).thenReturn(Collections.singleton(dataTemplate));

    mockMvc.perform(get(GET_LOT_DATA_TEMPLATES_PATH, AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.size()", is(1)))
        .andExpect(jsonPath("$.[0].criteria", is(expectedTemplatePayload)));
  }

  @Test
  void testGetDataTemplatesNotFound() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(null);

    when(businessLogicClient.getEventDataTemplates(AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI)).thenThrow(new LotNotFoundException(LOT1_NUMBER, AGREEMENT_NUMBER));

    mockMvc.perform(get(GET_LOT_DATA_TEMPLATES_PATH, AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(LotNotFoundException.ERROR_MSG_TEMPLATE, LOT1_NUMBER,
                AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testGetDocumentTemplates() throws Exception {
    final var templateUrl = "http://url";
    var doc = new Document();
    doc.setUrl(templateUrl);
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(mockLot);
    when(mockLot.getProcurementQuestionTemplates()).thenReturn(lotProcurementQuestionTemplates);

    when(businessLogicClient.getEventDocumentTemplates(AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI)).thenReturn(Collections.singleton(doc));

    mockMvc
        .perform(
            get(GET_LOT_DOCUMENT_TEMPLATES_PATH, AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.size()", is(1)))
        .andExpect(jsonPath("$.[0].url", is(templateUrl)));
  }

  @Test
  void testGetDocumentTemplatesNotFound() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(null);

    when(businessLogicClient.getEventDocumentTemplates(AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI)).thenThrow(new LotNotFoundException(LOT1_NUMBER, AGREEMENT_NUMBER));

    mockMvc
        .perform(
            get(GET_LOT_DOCUMENT_TEMPLATES_PATH, AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].status", is(HttpStatus.NOT_FOUND.toString())))
        .andExpect(jsonPath("$.errors[0].title", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_TITLE)))
        .andExpect(jsonPath("$.errors[0].detail",
            is(String.format(LotNotFoundException.ERROR_MSG_TEMPLATE, LOT1_NUMBER,
                AGREEMENT_NUMBER))))
        .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_NOT_FOUND_DESCRIPTION)));
  }

  @Test
  void testCreateLotValid() throws Exception {

    final String AGREEMENT_NUMBER = "RM1045";
    LotDetail lotDetail = new LotDetail("11", "Lot 11 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);
    LotDetail result = new LotDetail("11", "Lot 11 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);

    when(businessLogicClient.saveLot(lotDetail, AGREEMENT_NUMBER, lotDetail.getNumber())).thenReturn(result);

    mockMvc.perform(MockMvcRequestBuilders
                    .put(String.format("/agreements/%s/lots/%s", AGREEMENT_NUMBER, lotDetail.getNumber()))
                    .content(asJsonString(lotDetail))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("number", is(lotDetail.getNumber())))
            .andExpect(jsonPath("name", is(lotDetail.getName())))
            .andExpect(jsonPath("description", is(lotDetail.getDescription())))
            .andExpect(jsonPath("startDate", is(lotDetail.getStartDate().toString())))
            .andExpect(jsonPath("endDate", is(lotDetail.getEndDate().toString())))
            .andExpect(jsonPath("type", is(lotDetail.getType().getName())));
  }

  @Test
  void testCreateLotInvalidEmptyDescription() throws Exception {

    final String AGREEMENT_NUMBER = "RM1045";
    LotDetail lotDetail = new LotDetail("11", "Lot 11 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);
    lotDetail.setDescription(null);

    when(businessLogicClient.saveLot(lotDetail, AGREEMENT_NUMBER, lotDetail.getNumber())).thenThrow(new InvalidLotException("description", lotDetail.getNumber()));

    mockMvc.perform(MockMvcRequestBuilders
                    .put(String.format("/agreements/%s/lots/%s", AGREEMENT_NUMBER, lotDetail.getNumber()))
                    .content(asJsonString(lotDetail))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION)))
            .andExpect(jsonPath("$.errors..detail", is(new ArrayList<String>(List.of(new String[]{"Invalid lot format, missing 'description' for Lot 11"})))));
  }

  LotSupplier setupLotSupplier(){
    OrganizationIdentifier oi = new OrganizationIdentifier();
    oi.setLegalName("First company ever");
    oi.setScheme(Scheme.GBCHC);
    oi.setId("1234567");
    OrganizationDetail od = new OrganizationDetail();
    od.setActive(true);
    od.setCountryCode("GB");
    od.setCreationDate(LocalDate.now());
    Address add = new Address();
    add.setStreetAddress("Street Name");
    add.setPostalCode("ABC123");
    add.setCountryCode("GB");
    add.setCountryName("United Kingdom");
    ContactPoint cp = new ContactPoint();
    cp.setName("Person Incharge");
    cp.setEmail("Test@email.com");
    cp.setTelephone("0123456789");
    cp.setFaxNumber("6574839201");
    cp.setUrl("www.url.co.uk");

    Organization org = new Organization();
    org.setIdentifier(oi);
    org.setDetails(od);
    org.setAddress(add);
    org.setContactPoint(cp);

    LotSupplier lotSupplier = new LotSupplier();
    lotSupplier.setOrganization(org);
    lotSupplier.setSupplierStatus(SupplierStatus.ACTIVE);
    lotSupplier.setLastUpdatedBy("Local Macbook");

    return lotSupplier;
  }

  SupplierSummary setupSupplierSummary(){
    SupplierSummary ss = new SupplierSummary();
    ss.setLastUpdatedBy("Local Macbook");
    ss.setLastUpdatedDate(LocalDate.now());
    ss.setSupplierCount(1);
    
    return ss;
  }

  @Test
  void testAddLotSupplierWithContactDetail() throws Exception {

    final String AGREEMENT_NUMBER = "RM1045";
    final String LOT_NUMBER= "LOT1_NUMBER";

    LotSupplier lotSupplier = setupLotSupplier();
    final Set<LotSupplier> lotSupplierSet = Collections.singleton(lotSupplier);
    final SupplierSummary supplierSummary = setupSupplierSummary();

    when(businessLogicClient.saveLotSuppliers(AGREEMENT_NUMBER, LOT_NUMBER, lotSupplierSet)).thenReturn(supplierSummary);

    mockMvc.perform(MockMvcRequestBuilders
                    .put(String.format("/agreements/%s/lots/%s/suppliers", AGREEMENT_NUMBER, LOT_NUMBER))
                    .content(asJsonString(lotSupplierSet))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastUpdatedDate", is(supplierSummary.getLastUpdatedDate().toString())))
            .andExpect(jsonPath("$.lastUpdatedBy", is(supplierSummary.getLastUpdatedBy())))
            .andExpect(jsonPath("$.supplierCount", is(supplierSummary.getSupplierCount())));
  }

  @Test
  void testAddLotSupplierWithoutContactDetail() throws Exception {

    final String AGREEMENT_NUMBER = "RM1045";
    final String LOT_NUMBER= "LOT1_NUMBER";

    LotSupplier lotSupplier = setupLotSupplier();
    lotSupplier.getOrganization().setAddress(null);
    lotSupplier.getOrganization().setContactPoint(null);

    final Set<LotSupplier> lotSupplierSet = Collections.singleton(lotSupplier);
    final SupplierSummary supplierSummary = setupSupplierSummary();


    when(businessLogicClient.saveLotSuppliers(AGREEMENT_NUMBER, LOT_NUMBER, lotSupplierSet)).thenReturn(supplierSummary);

    mockMvc.perform(MockMvcRequestBuilders
                    .put(String.format("/agreements/%s/lots/%s/suppliers", AGREEMENT_NUMBER, LOT_NUMBER))
                    .content(asJsonString(lotSupplierSet))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastUpdatedDate", is(supplierSummary.getLastUpdatedDate().toString())))
            .andExpect(jsonPath("$.lastUpdatedBy", is(supplierSummary.getLastUpdatedBy())))
            .andExpect(jsonPath("$.supplierCount", is(supplierSummary.getSupplierCount())));
  }

  @Test
  void testAddInvalidLotSupplier() throws Exception {

    final String AGREEMENT_NUMBER = "RM1045";
    final String LOT_NUMBER= "LOT1_NUMBER";

    LotSupplier lotSupplier = setupLotSupplier();
    lotSupplier.getOrganization().getIdentifier().setLegalName(null);

    final Set<LotSupplier> lotSupplierSet = Collections.singleton(lotSupplier);

    when(businessLogicClient.saveLotSuppliers(AGREEMENT_NUMBER, LOT_NUMBER, lotSupplierSet)).thenThrow(new InvalidOrganisationException("legalName"));

    mockMvc.perform(MockMvcRequestBuilders
                    .put(String.format("/agreements/%s/lots/%s/suppliers", AGREEMENT_NUMBER, LOT_NUMBER))
                    .content(asJsonString(lotSupplierSet))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.description", is(GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION)))
            .andExpect(jsonPath("$.errors..detail", is(new ArrayList<String>(List.of(new String[]{"Invalid organisation format, missing 'legalName'"})))));
  }

  @Test
  void testUpdateLotEventTypes() throws Exception {

    final String AGREEMENT_NUMBER = "RM1045";
    final String LOT_NUMBER= "LOT1_NUMBER";

    LotEventTypeUpdate lotEventTypeUpdate = new LotEventTypeUpdate();
    lotEventTypeUpdate.setType("PA");
    lotEventTypeUpdate.setMandatoryEvent(false);
    lotEventTypeUpdate.setRepeatableEvent(true);
    lotEventTypeUpdate.setAssessmentToolId("FCA_TOOL_1");
    lotEventTypeUpdate.setMaxRepeats(1);

    when(businessLogicClient.updateLotEventTypes(AGREEMENT_NUMBER, LOT_NUMBER, any(LotEventTypeUpdate.class))).thenReturn(businessLogicClient.getLotEventTypes(AGREEMENT_NUMBER, LOT_NUMBER));

    mockMvc.perform(MockMvcRequestBuilders
                    .put(String.format("/agreements/%s/lots/%s/event-types", AGREEMENT_NUMBER, LOT_NUMBER))
                    .content(asJsonString(lotEventTypeUpdate))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(asJsonString(businessLogicClient.getLotEventTypes(AGREEMENT_NUMBER, LOT_NUMBER))));
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
