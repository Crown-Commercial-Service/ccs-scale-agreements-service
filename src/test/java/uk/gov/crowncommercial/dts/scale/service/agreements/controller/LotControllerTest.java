package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
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

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AgreementService service;

  @MockBean
  private QuestionTemplateService templateService;

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

  @MockBean
  private LotProcurementEventType lotProcurementEventType;

  @MockBean
  private Set<LotProcurementQuestionTemplate> lotProcurementQuestionTemplates;

  @Test
  void testGetLotSuccess() throws Exception {
    final var lot = new LotDetail();
    lot.setNumber(LOT1_NUMBER);

    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(mockLot);
    when(converter.convertLotToDTO(mockLot)).thenReturn(lot);
    mockMvc.perform(get(GET_LOT_PATH, AGREEMENT_NUMBER, LOT1_NUMBER)).andExpect(status().isOk())
        .andExpect(jsonPath("$.number", is(LOT1_NUMBER)));
  }

  @Test
  void testGetLotNotFound() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(null);
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
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
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
    when(converter.convertLotProcurementEventTypesToDTOs(lotProcurementEventTypes))
        .thenReturn(Collections.singleton(eventType));

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
    when(converter.convertLotProcurementQuestionTemplateToDataTemplates(
        lotProcurementQuestionTemplates, EVENT_TYPE_RFI))
            .thenReturn(Collections.singleton(dataTemplate));
    when(templateService.getDataTemplates(mockLot, EVENT_TYPE_RFI))
            .thenReturn(Collections.singleton(dataTemplate));
    mockMvc.perform(get(GET_LOT_DATA_TEMPLATES_PATH, AGREEMENT_NUMBER, LOT1_NUMBER, EVENT_TYPE_RFI))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.size()", is(1)))
        .andExpect(jsonPath("$.[0].criteria", is(expectedTemplatePayload)));
  }

  @Test
  void testGetDataTemplatesNotFound() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT1_NUMBER))
        .thenReturn(null);
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
    when(converter.convertLotProcurementQuestionTemplateToDocumentTemplates(
        lotProcurementQuestionTemplates, EVENT_TYPE_RFI)).thenReturn(Collections.singleton(doc));
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
}
