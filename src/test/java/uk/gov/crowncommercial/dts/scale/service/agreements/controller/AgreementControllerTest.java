package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rollbar.notifier.Rollbar;

import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

@WebMvcTest(AgreementController.class)
public class AgreementControllerTest {

  private static final String AGREEMENT_NUMBER = "RM1000";
  private static final String LOT_NUMBER = "Lot 1";

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
  private Rollbar rollbar;

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
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/documents"))
        .andExpect(status().is4xxClientError()).andExpect(
            content().string(containsString(AgreementController.METHOD_NOT_IMPLEMENTED_MSG)));
  }

  @Test
  public void testGetAgreementUpdates() throws Exception {
    this.mockMvc.perform(get("/agreements/" + AGREEMENT_NUMBER + "/updates"))
        .andExpect(status().is4xxClientError()).andExpect(
            content().string(containsString(AgreementController.METHOD_NOT_IMPLEMENTED_MSG)));
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
