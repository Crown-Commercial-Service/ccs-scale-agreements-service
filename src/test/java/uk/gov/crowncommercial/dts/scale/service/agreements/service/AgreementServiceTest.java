package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotEventTypeUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureCache
class AgreementServiceTest {
    private static final String AGREEMENT_NUMBER = "RM1000";
  private static final String LOT_NUMBER = "Lot 1";
  private static final String LOT_NAME = "Name of Lot";
  private static final Integer TEMPLATE_ID = 1;

    @MockitoBean
    private EhcacheConfig cacheConfig;

  @MockitoBean
  private CommercialAgreementRepo mockCommercialAgreementRepo;

  @MockitoBean
  private CommercialAgreementBenefitRepo mockCommercialAgreementBenefitRepo;

  @MockitoBean
  private CommercialAgreementBenefitService mockCommercialAgreementBenefitService;

  @MockitoBean
  private CommercialAgreement mockCommercialAgreement;

  @MockitoBean
  private ProcurementQuestionTemplate mockProcurementQuestionTemplate;

  @MockitoBean
  private List<CommercialAgreement> mockCommercialAgreements;

  @MockitoBean
  private LotRepo mockLotRepo;

  @MockitoBean
  private ProcurementQuestionTemplateRepo mockProcurementQuestionTemplateRepo;

  @MockitoBean
  private Lot mockLot;

  @MockitoBean
  private Collection<LotProcurementQuestionTemplate> mockTemplates;

  @MockitoBean
  private Set<CommercialAgreementBenefit> mockCommercialAgreementBenefitCollection;

  @MockitoBean
  private LotProcurementEventTypeRepo mockLotProcurementEventTypeRepo;

  @Autowired
  AgreementService service;

  @Test
  void testGetAgreement() {
    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER))
        .thenReturn(Optional.ofNullable(mockCommercialAgreement));
    assertEquals(mockCommercialAgreement, service.findAgreementByNumber(AGREEMENT_NUMBER));
  }

  @Test
  void testGetAgreementNotFound() {
    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.empty());

    AgreementNotFoundException thrown = Assertions.assertThrows(
            AgreementNotFoundException.class,
            () -> service.findAgreementByNumber(AGREEMENT_NUMBER),
            "Agreement Not Found exception was not thrown"
    );

    assertTrue(thrown.getMessage().contains("not found"));
  }

  @Test
  void testGetAgreements() {
    when(mockCommercialAgreementRepo.findAll()).thenReturn(mockCommercialAgreements);
    assertEquals(mockCommercialAgreements, service.getAgreements());
  }

  @Test
  void testGetLot() {
    when(mockLotRepo.findByAgreementNumberAndNumber(AGREEMENT_NUMBER, LOT_NUMBER))
        .thenReturn(Optional.ofNullable(mockLot));
    assertEquals(mockLot,
        service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER));
  }

  @Test
  void testGetLotNotFound() {
    when(mockLotRepo.findByAgreementNumberAndNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenReturn(Optional.empty());

    LotNotFoundException thrown = Assertions.assertThrows(
            LotNotFoundException.class,
            () -> service.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER),
            "Lot Not Found exception was not thrown"
    );

    assertTrue(thrown.getMessage().contains("not found"));
  }

  @Test
  void testUpdateAgreementWithBenefits() {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    Set<CommercialAgreementBenefit> benefits = new LinkedHashSet<>();

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
            .thenReturn(Optional.of(result));
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
  void testUpdateAgreementWithoutBenefits() {

    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.of(result));

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
  void testUpdateAgreementWithRegulationAndAgreementType() {

    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    result.setRegulation("PCR2006");
    result.setAgreementType("PCR06 Framework");

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.of(result));

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
  void testCreateAgreementWithoutBenefits() {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.empty()).thenReturn(Optional.of(result));

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
  void testCreateAgreementWithRegulation() {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    input.setRegulation("PCR2015");
    result.setRegulation("PCR2015");

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.empty()).thenReturn(Optional.of(result));

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
  void testCreateAgreementWithRegulationAndAgreementType() {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    input.setRegulation("PCR2015");
    input.setAgreementType("Closed Framework");
    result.setRegulation("PCR2015");
    result.setAgreementType("Closed Framework");


    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.empty()).thenReturn(Optional.of(result));

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
  void testCreateAgreementWithBenefit() {

    CommercialAgreement input = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);
    CommercialAgreement result = new CommercialAgreement(AGREEMENT_NUMBER,"Technology Products 2", "GCA", "Short textual description of the commercial agreement", LocalDate.of(2012, 11, 25), java.time.LocalDate.now().plusDays(5), "URL", true);

    Set<CommercialAgreementBenefit> benefits = new LinkedHashSet<>();

    CommercialAgreementBenefit cab1 = new CommercialAgreementBenefit();
    cab1.setName("Benefit 1");
    cab1.setDescription("Benefit 1");
    cab1.setSequence(1);
    benefits.add(cab1);

    input.setBenefits(benefits);
    result.setBenefits(benefits);

    when(mockCommercialAgreementRepo.findByNumber(AGREEMENT_NUMBER)).thenReturn(Optional.empty()).thenReturn(Optional.of(result));

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
  void testCreateLot() {

    Lot lot = new Lot(LOT_NUMBER, "Just a Name", "Some description", "PRODUCT", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), mockCommercialAgreement);

    when(mockLotRepo.findByAgreementNumberAndNumber(mockCommercialAgreement.getNumber(), LOT_NUMBER)).thenReturn(Optional.empty()).thenReturn(Optional.of(lot));
    Lot result = service.createOrUpdateLot(lot);

    assertEquals(lot.getName(), result.getName());
    assertEquals(lot.getNumber(), result.getNumber());
    assertEquals(lot.getDescription(), result.getDescription());
    assertEquals(lot.getStartDate(), result.getStartDate());
    assertEquals(lot.getEndDate(), result.getEndDate());
    assertEquals(lot.getLotType(), result.getLotType());
  }

  @Test
  void testUpdateLot() {

    Lot lot = new Lot(LOT_NUMBER, "Just a Name", "Some description", "SERVICE", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), mockCommercialAgreement);

    when(mockLotRepo.findByAgreementNumberAndNumber(mockCommercialAgreement.getNumber(), LOT_NUMBER)).thenReturn(Optional.of(lot));

    Lot result = service.createOrUpdateLot(lot);

    assertEquals(lot.getName(), result.getName());
    assertEquals(lot.getNumber(), result.getNumber());
    assertEquals(lot.getDescription(), result.getDescription());
    assertEquals(lot.getStartDate(), result.getStartDate());
    assertEquals(lot.getEndDate(), result.getEndDate());
    assertEquals(lot.getLotType(), result.getLotType());
  }

  @Test
  void testCreateQuestionDataTemplate() {

    Map<String, Object> payload = new HashMap<>();
    payload.put("id", "Criterion 1");
    payload.put("title", "About the procurement competition");
    payload.put("source", "buyer");
    payload.put("relatesTo", "buyer");
    payload.put("description", "For Information Only");
    payload.put("requirementGroups", new ArrayList<>());

    ProcurementQuestionTemplate procurementQuestionTemplate = new ProcurementQuestionTemplate();
    procurementQuestionTemplate.setTemplatePayload(List.of(payload));
    procurementQuestionTemplate.setId(1);
    procurementQuestionTemplate.setTemplateName("TestTemplateName");
    procurementQuestionTemplate.setDescription("Some description");
    procurementQuestionTemplate.setParent(2);
    procurementQuestionTemplate.setMandatory(false);
    procurementQuestionTemplate.setCreatedBy("agreements-service-testing");
    procurementQuestionTemplate.setTemplateUrl("https//www.test.com/");
    procurementQuestionTemplate.setCreatedAt(java.time.LocalDateTime.now());
    procurementQuestionTemplate.setUpdatedAt(java.time.LocalDateTime.now());

    when(mockProcurementQuestionTemplateRepo.findById(TEMPLATE_ID)).thenReturn(Optional.empty()).thenReturn(Optional.of(procurementQuestionTemplate));
    ProcurementQuestionTemplate result = service.createOrUpdateProcurementDataTemplate(procurementQuestionTemplate);

    assertEquals(procurementQuestionTemplate.getTemplateName(), result.getTemplateName());
    assertEquals(procurementQuestionTemplate.getId(), result.getId());
    assertEquals(procurementQuestionTemplate.getDescription(), result.getDescription());
    assertEquals(procurementQuestionTemplate.getMandatory(), result.getMandatory());
    assertEquals(procurementQuestionTemplate.getCreatedBy(), result.getCreatedBy());
    assertEquals(procurementQuestionTemplate.getCreatedAt(), result.getCreatedAt());
    assertEquals(procurementQuestionTemplate.getUpdatedAt(), result.getUpdatedAt());
  }

  @Test
  void testUpdateQuestionDataTemplate() {

    Map<String, Object> payload = new HashMap<>();
    payload.put("id", "Criterion 2");
    payload.put("title", "New Criterion Update");
    payload.put("source", "buyer");
    payload.put("relatesTo", "buyer");
    payload.put("description", "For Information Only");
    payload.put("requirementGroups", new ArrayList<>());

    ProcurementQuestionTemplate procurementQuestionTemplate = new ProcurementQuestionTemplate();
    procurementQuestionTemplate.setTemplatePayload(List.of(payload));
    procurementQuestionTemplate.setId(1); //Same ID for update instead of create
    procurementQuestionTemplate.setTemplateName("UpdatedTemplateName");
    procurementQuestionTemplate.setDescription("New Updated description");
    procurementQuestionTemplate.setParent(3);
    procurementQuestionTemplate.setMandatory(true);
    procurementQuestionTemplate.setCreatedBy("agreements-service-testing");
    procurementQuestionTemplate.setTemplateUrl("https//www.test-updated.com/");
    procurementQuestionTemplate.setCreatedAt(java.time.LocalDateTime.now());
    procurementQuestionTemplate.setUpdatedAt(java.time.LocalDateTime.now());

    when(mockProcurementQuestionTemplateRepo.findById(TEMPLATE_ID)).thenReturn(Optional.of(procurementQuestionTemplate));
    ProcurementQuestionTemplate result = service.createOrUpdateProcurementDataTemplate(procurementQuestionTemplate);

    assertEquals(procurementQuestionTemplate.getTemplateName(), result.getTemplateName());
    assertEquals(procurementQuestionTemplate.getId(), result.getId());
    assertEquals(procurementQuestionTemplate.getDescription(), result.getDescription());
    assertEquals(procurementQuestionTemplate.getMandatory(), result.getMandatory());
    assertEquals(procurementQuestionTemplate.getCreatedBy(), result.getCreatedBy());
    assertEquals(procurementQuestionTemplate.getCreatedAt(), result.getCreatedAt());
    assertEquals(procurementQuestionTemplate.getUpdatedAt(), result.getUpdatedAt());
  }

  @Test
  void testUpdateLotEventTypes() {

    Lot lot = new Lot();
    lot.setNumber(LOT_NUMBER);
    lot.setName(LOT_NAME);
    lot.setLotType("service");

    ProcurementEventType procurementEventType = new ProcurementEventType();
    procurementEventType.setId(1);
    procurementEventType.setDescription("Testing");
    procurementEventType.setName("PA");
    procurementEventType.setPreMarketActivity(true);

    LotProcurementEventTypeKey lotProcurementEventTypeKey = new LotProcurementEventTypeKey();
    lotProcurementEventTypeKey.setLotId(lot.getId());
    lotProcurementEventTypeKey.setProcurementEventTypeId(procurementEventType.getId());

    LotProcurementEventType lotProcurementEventType = new LotProcurementEventType();
    lotProcurementEventType.setKey(lotProcurementEventTypeKey);
    lotProcurementEventType.setLot(lot);
    lotProcurementEventType.setProcurementEventType(procurementEventType);
    lotProcurementEventType.setIsMandatoryEvent(false);
    lotProcurementEventType.setIsRepeatableEvent(true);
    lotProcurementEventType.setMaxRepeats(1);
    lotProcurementEventType.setAssessmentToolId("FCA_TOOL_1");

    LotEventTypeUpdate lotEventTypeUpdate = new LotEventTypeUpdate();
    lotEventTypeUpdate.setType("PA");
    lotEventTypeUpdate.setMandatoryEvent(false);
    lotEventTypeUpdate.setRepeatableEvent(true);
    lotEventTypeUpdate.setMaxRepeats(1);
    lotEventTypeUpdate.setAssessmentToolId("FCA_TOOL_1");

    when(mockLotProcurementEventTypeRepo.findById(TEMPLATE_ID)).thenReturn(Optional.of(lotProcurementEventType));
    LotProcurementEventType result = service.updateLotEventTypes(lot, procurementEventType, lotEventTypeUpdate);

    assertEquals(lotProcurementEventType.getLot(), result.getLot());
    assertEquals(lotProcurementEventType.getProcurementEventType(), result.getProcurementEventType());
    assertEquals(lotProcurementEventType.getIsMandatoryEvent(), result.getIsMandatoryEvent());
    assertEquals(lotProcurementEventType.getIsRepeatableEvent(), result.getIsRepeatableEvent());
    assertEquals(lotProcurementEventType.getMaxRepeats(), result.getMaxRepeats());
    assertEquals(lotProcurementEventType.getAssessmentToolId(), result.getAssessmentToolId());
  }

    @Test
    public void testCreateOrUpdateEventTypeDoesNotError() throws Exception {
        ProcurementEventType mockModel = new ProcurementEventType();
        mockModel.setId(99);
        mockModel.setDescription("Testing");
        mockModel.setName("PA");
        mockModel.setPreMarketActivity(true);

        service.createOrUpdateEventType(mockModel);

        assertDoesNotThrow(() -> service.createOrUpdateEventType(mockModel));
    }
}