package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementQuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.LotRepo;

@SpringBootTest
@ActiveProfiles("test")
class AgreementServiceTest {

  private static final String AGREEMENT_NUMBER = "RM1000";
  private static final String LOT_NUMBER = "Lot 1";
  private static final String EVENT_TYPE = "RFI";

  @MockBean
  private CommercialAgreementRepo mockCommercialAgreementRepo;

  @MockBean
  private CommercialAgreement mockCommercialAgreement;

  @MockBean
  private List<CommercialAgreement> mockCommercialAgreements;

  @MockBean
  private LotRepo mockLotRepo;

  @MockBean
  private Lot mockLot;

  @MockBean
  private Collection<LotProcurementQuestionTemplate> mockTemplates;

  @Autowired
  AgreementService service;

  @Test
  void testGetAgreement() throws Exception {
    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER))
        .thenReturn(mockCommercialAgreement);
    assertEquals(mockCommercialAgreement, service.findAgreementByNumber(AGREEMENT_NUMBER));
  }

  @Test
  void testGetAgreementNotFound() throws Exception {
    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(null);

    AgreementNotFoundException thrown = Assertions.assertThrows(
            AgreementNotFoundException.class,
            () -> service.findAgreementByNumber(AGREEMENT_NUMBER),
            "Agreement Not Found exception was not thrown"
    );

    assertTrue(thrown.getMessage().contains("not found"));
  }

  @Test
  void testGetAgreements() throws Exception {
    when(mockCommercialAgreementRepo.findAll()).thenReturn(mockCommercialAgreements);
    assertEquals(mockCommercialAgreements, service.getAgreements());
  }

  @Test
  void testGetLot() throws Exception {
    when(mockLotRepo.findByAgreementNumberAndNumber(AGREEMENT_NUMBER, LOT_NUMBER))
        .thenReturn(mockLot);
    assertEquals(mockLot,
        service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER));
  }

  @Test
  void testGetLotNotFound() throws Exception {
    when(mockLotRepo.findByAgreementNumberAndNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenReturn(null);

    LotNotFoundException thrown = Assertions.assertThrows(
            LotNotFoundException.class,
            () -> service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER),
            "Lot Not Found exception was not thrown"
    );

    assertTrue(thrown.getMessage().contains("not found"));
  }
}
