package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementDocument;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

@WebMvcTest(AgreementController.class)
public class AgreementControllerTest {

  private static final String AGREEMENT_NUMBER = "RM1000";
  private static final String LOT_NUMBER = "Lot 1";
  private static final String DOCUMENT_NAME = "My Doc";
  private static final String AGREEMENT_UPDATE_TEXT = "Update Text";

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

  @Test
  public void testGetAgreementSummariesSuccess() throws Exception {
    AgreementSummary agreement = new AgreementSummary();
    agreement.setNumber(AGREEMENT_NUMBER);

    when(converter.convertAgreementToSummaryDTO(mockCommercialAgreement)).thenReturn(agreement);
    when(service.getAgreements()).thenReturn(Arrays.asList(mockCommercialAgreement));
    this.mockMvc.perform(get("/agreements")).andExpect(status().isOk())
        .andExpect(content().string(containsString(AGREEMENT_NUMBER)));
  }

  @Test
  public void testGetAgreementSuccess() throws Exception {
    AgreementDetail agreement = new AgreementDetail();
    agreement.setNumber(AGREEMENT_NUMBER);

    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(converter.convertAgreementToDTO(mockCommercialAgreement)).thenReturn(agreement);
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER)).andExpect(status().isOk())
        .andExpect(content().string(containsString(AGREEMENT_NUMBER)));
  }

  @Test
  public void testGetAgreementNotFound() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(null);
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(containsString("Agreement number 'RM1000' not found")));
  }

  @Test
  public void testGetAgreementUnexpectedError() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER))
        .andExpect(status().is5xxServerError())
        .andExpect(content().string(containsString("An error occurred processing the request")));
  }

  @Test
  public void testGetAgreementLotsSuccess() throws Exception {
    LotDetail lot = new LotDetail();
    lot.setNumber(LOT_NUMBER);
    Set<Lot> mockLots = new HashSet<>(Arrays.asList(mockLot));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getLots()).thenReturn(mockLots);
    when(converter.convertLotsToDTOs(mockLots)).thenReturn(Arrays.asList(lot));
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/lots"))
        .andExpect(status().isOk()).andExpect(content().string(containsString(LOT_NUMBER)));
  }

  @Test
  public void testGetAgreementLotsNotFound() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(null);
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(containsString("Agreement number 'RM1000' not found")));
  }

  @Test
  public void testGetAgreementLotsUnexpectedError() throws Exception {
    when(service.findAgreementByNumber(AGREEMENT_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/lots"))
        .andExpect(status().is5xxServerError())
        .andExpect(content().string(containsString("An error occurred processing the request")));
  }

  @Test
  public void testGetLotSuccess() throws Exception {
    LotDetail lot = new LotDetail();
    lot.setNumber(LOT_NUMBER);

    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER))
        .thenReturn(mockLot);
    when(converter.convertLotToDTO(mockLot)).thenReturn(lot);
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/lots/" + LOT_NUMBER))
        .andExpect(status().isOk()).andExpect(content().string(containsString(LOT_NUMBER)));
  }

  @Test
  public void testGetLotNotFound() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER))
        .thenReturn(null);
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/lots/" + LOT_NUMBER))
        .andExpect(status().is4xxClientError()).andExpect(content()
            .string(containsString("Lot number 'Lot 1' for agreement number 'RM1000' not found")));
  }

  @Test
  public void testGetLotUnexpectedError() throws Exception {
    when(service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER))
        .thenThrow(new RuntimeException("Something is amiss"));
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/lots/" + LOT_NUMBER))
        .andExpect(status().is5xxServerError())
        .andExpect(content().string(containsString("An error occurred processing the request")));
  }

  @Test
  public void testGetAgreementDocuments() throws Exception {
    Document document = new Document();
    document.setName(DOCUMENT_NAME);
    Set<CommercialAgreementDocument> mockDocs =
        new HashSet<>(Arrays.asList(mockCommercialAgreementDocument));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getDocuments()).thenReturn(mockDocs);
    when(converter.convertAgreementDocumentsToDTOs(mockDocs)).thenReturn(Arrays.asList(document));
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/documents"))
        .andExpect(status().isOk()).andExpect(content().string(containsString(DOCUMENT_NAME)));
  }

  @Test
  public void testGetAgreementUpdates() throws Exception {
    AgreementUpdate update = new AgreementUpdate();
    update.setText(AGREEMENT_UPDATE_TEXT);
    Set<CommercialAgreementUpdate> mockUpdates =
        new HashSet<>(Arrays.asList(mockCommercialAgreementUpdate));
    when(service.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
    when(mockCommercialAgreement.getUpdates()).thenReturn(mockUpdates);
    when(converter.convertAgreementUpdatesToDTO(mockUpdates)).thenReturn(Arrays.asList(update));
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/updates"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(AGREEMENT_UPDATE_TEXT)));
  }

  @Test
  public void testGetLotDocuments() throws Exception {
    this.mockMvc
        .perform(get("/agreements/" + AGREEMENT_NUMBER + "/lots/" + LOT_NUMBER + "/documents"))
        .andExpect(status().is4xxClientError()).andExpect(
            content().string(containsString(AgreementController.METHOD_NOT_IMPLEMENTED_MSG)));
  }

  @Test
  public void testGetLotSuppliers() throws Exception {
    this.mockMvc
        .perform(get("/agreements/" + AGREEMENT_NUMBER + "/lots/" + LOT_NUMBER + "/suppliers"))
        .andExpect(status().is4xxClientError()).andExpect(
            content().string(containsString(AgreementController.METHOD_NOT_IMPLEMENTED_MSG)));
  }
}
