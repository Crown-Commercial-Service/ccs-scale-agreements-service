package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementType;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Regulation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementBenefit;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementQuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementBenefitRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.LotRepo;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureCache
class AgreementServiceTest {
  @MockBean
  private EhcacheConfig cacheConfig;

  private static final String AGREEMENT_NUMBER = "RM1000";
  private static final String LOT_NUMBER = "Lot 1";

  @MockBean
  private CommercialAgreementRepo mockCommercialAgreementRepo;

  @MockBean
  private CommercialAgreementBenefitRepo mockCommercialAgreementBenefitRepo;

  @MockBean
  private CommercialAgreementBenefitService mockCommercialAgreementBenefitService;

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

  @MockBean
  private Set<CommercialAgreementBenefit> mockCommercialAgreementBenefitCollection;

  @Autowired
  AgreementService service;

  @Test
  void testGetAgreement() throws Exception {
    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER))
        .thenReturn(Optional.ofNullable(mockCommercialAgreement));
    assertEquals(mockCommercialAgreement, service.findAgreementByNumber(AGREEMENT_NUMBER));
  }

  @Test
  void testGetAgreementNotFound() throws Exception {
    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.ofNullable(null));

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
        .thenReturn(Optional.ofNullable(mockLot));
    assertEquals(mockLot,
        service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER));
  }

  @Test
  void testGetLotNotFound() throws Exception {
    when(mockLotRepo.findByAgreementNumberAndNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenReturn(Optional.ofNullable(null));

    LotNotFoundException thrown = Assertions.assertThrows(
            LotNotFoundException.class,
            () -> service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER),
            "Lot Not Found exception was not thrown"
    );

    assertTrue(thrown.getMessage().contains("not found"));
  }

  @Test
  void testUpdateAgreementWithBenefits() throws Exception {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    Set<CommercialAgreementBenefit> benefits = new LinkedHashSet<CommercialAgreementBenefit>();

    CommercialAgreementBenefit cab1 = new CommercialAgreementBenefit(), cab2 = new CommercialAgreementBenefit();
    cab1.setName("Benefit 1");
    cab1.setDescription("Benefit 1");
    cab1.setSequence(1);
    benefits.add(cab1);

    cab2.setName("Benefit 2");
    cab2.setDescription("Benefit 2");
    cab2.setSequence(2);
    benefits.add(cab2);

    input.setBenefits(benefits);

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER))
            .thenReturn(Optional.ofNullable(result));
    when(mockCommercialAgreementBenefitRepo.findByAgreement(input))
            .thenReturn(Optional.ofNullable(mockCommercialAgreementBenefitCollection));

    CommercialAgreement saveAgreement = service.createOrUpdateAgreement(input);

    assertEquals(saveAgreement.getName(), result.getName());
    assertEquals(saveAgreement.getNumber(), result.getNumber());
    assertEquals(saveAgreement.getOwner(), result.getOwner());
    assertEquals(saveAgreement.getDescription(), result.getDescription());
    assertEquals(saveAgreement.getStartDate(), result.getStartDate());
    assertEquals(saveAgreement.getEndDate(), result.getEndDate());
    assertEquals(saveAgreement.getDetailUrl(), result.getDetailUrl());
    assertEquals(saveAgreement.getBenefits(), result.getBenefits());
  }

  @Test
  void testUpdateAgreementWithoutBenefits() throws Exception {

    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER))
            .thenReturn(Optional.ofNullable(result));

    CommercialAgreement saveAgreement = service.createOrUpdateAgreement(result);

    assertEquals(saveAgreement.getName(), result.getName());
    assertEquals(saveAgreement.getNumber(), result.getNumber());
    assertEquals(saveAgreement.getOwner(), result.getOwner());
    assertEquals(saveAgreement.getDescription(), result.getDescription());
    assertEquals(saveAgreement.getStartDate(), result.getStartDate());
    assertEquals(saveAgreement.getEndDate(), result.getEndDate());
    assertEquals(saveAgreement.getDetailUrl(), result.getDetailUrl());
    assertEquals(saveAgreement.getBenefits(), result.getBenefits());
    for (CommercialAgreementBenefit benefit : saveAgreement.getBenefits()) {
      assertEquals(benefit.getAgreement(), result);
    }
  }

  @Test
  void testUpdateAgreementWithRegulationAndAgreementType() throws Exception {

    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    result.setRegulation("PCR2006");
    result.setAgreementType("PCR06 Framework");

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER))
            .thenReturn(Optional.ofNullable(result));

    CommercialAgreement saveAgreement = service.createOrUpdateAgreement(result);

    assertEquals(saveAgreement.getName(), result.getName());
    assertEquals(saveAgreement.getNumber(), result.getNumber());
    assertEquals(saveAgreement.getOwner(), result.getOwner());
    assertEquals(saveAgreement.getDescription(), result.getDescription());
    assertEquals(saveAgreement.getStartDate(), result.getStartDate());
    assertEquals(saveAgreement.getEndDate(), result.getEndDate());
    assertEquals(saveAgreement.getDetailUrl(), result.getDetailUrl());
    assertEquals(saveAgreement.getBenefits(), result.getBenefits());
    assertEquals(saveAgreement.getRegulation(), result.getRegulation());
    assertEquals(saveAgreement.getAgreementType(), result.getAgreementType());

    for (CommercialAgreementBenefit benefit : saveAgreement.getBenefits()) {
      assertEquals(benefit.getAgreement(), result);
    }
  }

  @Test
  void testCreateAgreementWithoutBenefits() throws Exception {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.ofNullable(null)).thenReturn(Optional.of(result));;

    CommercialAgreement saveAgreement = service.createOrUpdateAgreement(input);

    assertEquals(saveAgreement.getName(), result.getName());
    assertEquals(saveAgreement.getNumber(), result.getNumber());
    assertEquals(saveAgreement.getOwner(), result.getOwner());
    assertEquals(saveAgreement.getDescription(), result.getDescription());
    assertEquals(saveAgreement.getStartDate(), result.getStartDate());
    assertEquals(saveAgreement.getEndDate(), result.getEndDate());
    assertEquals(saveAgreement.getDetailUrl(), result.getDetailUrl());
    assertEquals(saveAgreement.getBenefits(), result.getBenefits());
  }

  @Test
  void testCreateAgreementWithRegulation() throws Exception {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    input.setRegulation("PCR2015");
    result.setRegulation("PCR2015");

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.ofNullable(null)).thenReturn(Optional.of(result));;

    CommercialAgreement saveAgreement = service.createOrUpdateAgreement(input);

    assertEquals(saveAgreement.getName(), result.getName());
    assertEquals(saveAgreement.getNumber(), result.getNumber());
    assertEquals(saveAgreement.getOwner(), result.getOwner());
    assertEquals(saveAgreement.getDescription(), result.getDescription());
    assertEquals(saveAgreement.getStartDate(), result.getStartDate());
    assertEquals(saveAgreement.getEndDate(), result.getEndDate());
    assertEquals(saveAgreement.getDetailUrl(), result.getDetailUrl());
    assertEquals(saveAgreement.getBenefits(), result.getBenefits());
    assertEquals(saveAgreement.getRegulation(), result.getRegulation());
  }

  @Test
  void testCreateAgreementWithRegulationAndAgreementType() throws Exception {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    input.setRegulation("PCR2015");
    input.setAgreementType("Closed Framework");
    result.setRegulation("PCR2015");
    result.setAgreementType("Closed Framework");


    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.ofNullable(null)).thenReturn(Optional.of(result));;

    CommercialAgreement saveAgreement = service.createOrUpdateAgreement(input);

    assertEquals(saveAgreement.getName(), result.getName());
    assertEquals(saveAgreement.getNumber(), result.getNumber());
    assertEquals(saveAgreement.getOwner(), result.getOwner());
    assertEquals(saveAgreement.getDescription(), result.getDescription());
    assertEquals(saveAgreement.getStartDate(), result.getStartDate());
    assertEquals(saveAgreement.getEndDate(), result.getEndDate());
    assertEquals(saveAgreement.getDetailUrl(), result.getDetailUrl());
    assertEquals(saveAgreement.getBenefits(), result.getBenefits());
    assertEquals(saveAgreement.getRegulation(), result.getRegulation());
    assertEquals(saveAgreement.getAgreementType(), result.getAgreementType());
  }

  @Test
  void testCreateAgreementWithBenefit() throws Exception {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "CCS", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    Set<CommercialAgreementBenefit> benefits = new LinkedHashSet<CommercialAgreementBenefit>();

    CommercialAgreementBenefit cab1 = new CommercialAgreementBenefit(), cab2 = new CommercialAgreementBenefit();
    cab1.setName("Benefit 1");
    cab1.setDescription("Benefit 1");
    cab1.setSequence(1);
    benefits.add(cab1);

    input.setBenefits(benefits);
    result.setBenefits(benefits);

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.ofNullable(null)).thenReturn(Optional.of(result));;

    CommercialAgreement saveAgreement = service.createOrUpdateAgreement(input);

    assertEquals(saveAgreement.getName(), result.getName());
    assertEquals(saveAgreement.getNumber(), result.getNumber());
    assertEquals(saveAgreement.getOwner(), result.getOwner());
    assertEquals(saveAgreement.getDescription(), result.getDescription());
    assertEquals(saveAgreement.getStartDate(), result.getStartDate());
    assertEquals(saveAgreement.getEndDate(), result.getEndDate());
    assertEquals(saveAgreement.getDetailUrl(), result.getDetailUrl());
    assertEquals(saveAgreement.getBenefits(), result.getBenefits());
  }


  @Test
  void testCreateLot() throws Exception {

    Lot lot = new Lot(LOT_NUMBER, "Just a Name", "Some description", "PRODUCT", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), mockCommercialAgreement);

    when(mockLotRepo.findByAgreementNumberAndNumber(mockCommercialAgreement.getNumber(), LOT_NUMBER)).thenReturn(Optional.ofNullable(null)).thenReturn(Optional.of(lot));
    Lot result = service.createOrUpdateLot(lot);

    assertEquals(lot.getName(), result.getName());
    assertEquals(lot.getNumber(), result.getNumber());
    assertEquals(lot.getDescription(), result.getDescription());
    assertEquals(lot.getStartDate(), result.getStartDate());
    assertEquals(lot.getEndDate(), result.getEndDate());
    assertEquals(lot.getLotType(), result.getLotType());
  }

  @Test
  void testUpdateLot() throws Exception {

    Lot lot = new Lot(LOT_NUMBER, "Just a Name", "Some description", "SERVICE", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), mockCommercialAgreement);

    when(mockLotRepo.findByAgreementNumberAndNumber(mockCommercialAgreement.getNumber(), LOT_NUMBER)).thenReturn(Optional.ofNullable(lot));

    Lot result = service.createOrUpdateLot(lot);

    assertEquals(lot.getName(), result.getName());
    assertEquals(lot.getNumber(), result.getNumber());
    assertEquals(lot.getDescription(), result.getDescription());
    assertEquals(lot.getStartDate(), result.getStartDate());
    assertEquals(lot.getEndDate(), result.getEndDate());
    assertEquals(lot.getLotType(), result.getLotType());
  }
}
