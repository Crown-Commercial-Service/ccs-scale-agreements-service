package uk.gov.crowncommercial.dts.scale.service.agreements.BLL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;
import uk.gov.crowncommercial.dts.scale.service.agreements.controller.GlobalErrorHandler;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidAgreementException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidLotException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.QuestionTemplateService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.WordpressService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureCache
public class BusinessLogicClientTests {
    @MockBean
    private EhcacheConfig cacheConfig;

    @MockBean
    private AgreementService agreementService;

    @MockBean
    private CommercialAgreement mockCommercialAgreement;

    @MockBean
    private Lot mockLot;

    @MockBean
    private LotOrganisationRole lotOrganisationRole;

    @MockBean
    private Set<LotProcurementQuestionTemplate> lotProcurementQuestionTemplates;

    @MockBean
    private WordpressService wordpressService;

    @MockBean
    private QuestionTemplateService questionTemplateService;

    @Autowired
    private BusinessLogicClient businessLogicClient;

    private static final String AGREEMENT_NUMBER = "RM3733";
    private static final String LOT_NUMBER = "Lot 1";
    private static final String DOCUMENT_NAME = "My Doc";
    private static final String DOCUMENT_URL = "http://document";
    private static final String DOCUMENT_TYPE = "tenderNotice";
    private static final String DOCUMENT_FORMAT = "text/html";
    private static final String DOCUMENT_LANGUAGE = "en_GB";
    private static final String DOCUMENT_DESCRIPTION = "Document Description";
    private static final Instant DOCUMENT_PUBLISHED_DATE = Instant.now();
    private static final Instant DOCUMENT_MODIFIED_DATE = Instant.now();
    private static final String EXCEPTION_FAILED_AGREEMENT_TEXT = "Agreement Not Found exception was not thrown";
    private static final String EXCEPTION_FAILED_LOT_TEXT = "Lot Not Found exception was not thrown";
    private static final String EXCEPTION_CHECK_TEXT = "not found";
    private static final String AGREEMENT_UPDATE_TEXT = "Update Text";
    private static final String EVENT_TYPE_RFI = "RFI";
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

    @Test
    void testGetAgreementsList() throws Exception {
        when(agreementService.getAgreements()).thenReturn(Arrays.asList(mockCommercialAgreement));

        List<AgreementSummary> result = businessLogicClient.getAgreementsList();

        assertEquals(result.size(), 1);
    }

    @Test
    void testGetAgreementDetailWithValidId() throws Exception {
        final AgreementDetail agreement = new AgreementDetail();
        agreement.setNumber(AGREEMENT_NUMBER);

        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
        when(wordpressService.getExpandedCommercialAgreement(any(), eq(AGREEMENT_NUMBER))).thenReturn(mockCommercialAgreement);

        AgreementDetail result = businessLogicClient.getAgreementDetail(AGREEMENT_NUMBER);

        assertNotNull(result);
    }

    @Test
    void testGetAgreementDetailWithInvalidId() throws Exception {
        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenThrow(new AgreementNotFoundException(AGREEMENT_NUMBER));

        AgreementNotFoundException thrown = Assertions.assertThrows(
                AgreementNotFoundException.class,
                () -> businessLogicClient.getAgreementDetail(AGREEMENT_NUMBER),
                EXCEPTION_FAILED_AGREEMENT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testGetLotsForAgreementWithValidIdNoBuyingMethod() throws Exception {
        final LotDetail lot = new LotDetail();
        lot.setNumber(LOT_NUMBER);

        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(getCommercialAgreementWithLot());

        Collection<LotDetail> result = businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.NONE);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    public CommercialAgreement getCommercialAgreementWithLot() {
        Set<LotRouteToMarket> lrtms = new HashSet<>();

        LotRouteToMarketKey key = new LotRouteToMarketKey();
        key.setLotId(1);
        key.setRouteToMarketName(ROUTE_TO_MARKET_NAME);

        RouteToMarket rtm = new RouteToMarket();
        rtm.setName(ROUTE_TO_MARKET_NAME);

        LotRouteToMarket lrtm = new LotRouteToMarket();
        lrtm.setRouteToMarket(rtm);
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

        lrtms.add(lrtm);

        Lot lot = new Lot();
        lot.setNumber(LOT_NUMBER);
        lot.setLotType("Products");
        lot.setRoutesToMarket(lrtms);

        Set<Lot> lots = new HashSet<>();
        lots.add(lot);

        CommercialAgreement ca = new CommercialAgreement();
        ca.setNumber(AGREEMENT_NUMBER);
        ca.setLots(lots);

        return ca;
    }

    @Test
    void testGetLotsForAgreementWithValidIdAndBuyingMethodFiltersResultsWithResults() throws Exception {
        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(getCommercialAgreementWithLot());

        Collection<LotDetail> result = businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.DIRECT_AWARD);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetLotsForAgreementWithValidIdAndBuyingMethodFiltersResultsWithNoResults() throws Exception {
        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(getCommercialAgreementWithLot());

        Collection<LotDetail> result = businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.E_AUCTION);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetLotsForAgreementWithInvalidId() throws Exception {
        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenThrow(new AgreementNotFoundException(AGREEMENT_NUMBER));

        AgreementNotFoundException thrown = Assertions.assertThrows(
                AgreementNotFoundException.class,
                () -> businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.NONE),
                EXCEPTION_FAILED_AGREEMENT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testGetDocumentsForAgreementWithValidId() throws Exception {
        final AgreementDetail agreement = new AgreementDetail();
        agreement.setNumber(AGREEMENT_NUMBER);

        final Document document = new Document();
        document.setName(DOCUMENT_NAME);
        document.setDescription(DOCUMENT_DESCRIPTION);
        document.setUrl(DOCUMENT_URL);
        document.setDocumentType(DOCUMENT_TYPE);
        document.setFormat(DOCUMENT_FORMAT);
        document.setLanguage(DOCUMENT_LANGUAGE);
        document.setPublishedDate(DOCUMENT_PUBLISHED_DATE);
        document.setModifiedDate(DOCUMENT_MODIFIED_DATE);

        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);

        Collection<Document> result = businessLogicClient.getDocumentsForAgreement(AGREEMENT_NUMBER);

        assertNotNull(result);
    }

    @Test
    void testGetDocumentsForAgreementWithInvalidId() throws Exception {
        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenThrow(new AgreementNotFoundException(AGREEMENT_NUMBER));

        AgreementNotFoundException thrown = Assertions.assertThrows(
                AgreementNotFoundException.class,
                () -> businessLogicClient.getDocumentsForAgreement(AGREEMENT_NUMBER),
                EXCEPTION_FAILED_AGREEMENT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testGetUpdatesForAgreementWithValidId() throws Exception {
        final AgreementDetail agreement = new AgreementDetail();
        agreement.setNumber(AGREEMENT_NUMBER);

        final AgreementUpdate update = new AgreementUpdate();
        update.setText(AGREEMENT_UPDATE_TEXT);

        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);

        Collection<AgreementUpdate> result = businessLogicClient.getUpdatesForAgreement(AGREEMENT_NUMBER);

        assertNotNull(result);
    }

    @Test
    void testGetUpdatesForAgreementWithInvalidId() throws Exception {
        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenThrow(new AgreementNotFoundException(AGREEMENT_NUMBER));

        AgreementNotFoundException thrown = Assertions.assertThrows(
                AgreementNotFoundException.class,
                () -> businessLogicClient.getUpdatesForAgreement(AGREEMENT_NUMBER),
                EXCEPTION_FAILED_AGREEMENT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testGetLotDetailWithValidId() throws Exception {
        final LotDetail lot = new LotDetail();
        lot.setNumber(LOT_NUMBER);

        when(agreementService.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenReturn(mockLot);
        when(wordpressService.getExpandedLot(any(), eq(AGREEMENT_NUMBER), eq(LOT_NUMBER))).thenReturn(mockLot);

        LotDetail result = businessLogicClient.getLotDetail(AGREEMENT_NUMBER, LOT_NUMBER);

        assertNotNull(result);
    }

    @Test
    void testGetLotDetailWithInvalidId() throws Exception {
        when(agreementService.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenThrow(new LotNotFoundException(AGREEMENT_NUMBER, LOT_NUMBER));

        LotNotFoundException thrown = Assertions.assertThrows(
                LotNotFoundException.class,
                () -> businessLogicClient.getLotDetail(AGREEMENT_NUMBER, LOT_NUMBER),
                EXCEPTION_FAILED_LOT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testGetLotSuppliersWithValidId() throws Exception {
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

        when(agreementService.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenReturn(lotOrgRoles);

        Collection<LotSupplier> result = businessLogicClient.getLotSuppliers(AGREEMENT_NUMBER, LOT_NUMBER);

        assertNotNull(result);
    }

    @Test
    void testGetLotSuppliersWithInvalidId() throws Exception {
        when(agreementService.findLotSupplierOrgRolesByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenThrow(new LotNotFoundException(AGREEMENT_NUMBER, LOT_NUMBER));

        LotNotFoundException thrown = Assertions.assertThrows(
                LotNotFoundException.class,
                () -> businessLogicClient.getLotSuppliers(AGREEMENT_NUMBER, LOT_NUMBER),
                EXCEPTION_FAILED_LOT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testGetLotEventTypesWithValidId() throws Exception {
        final LotDetail lot = new LotDetail();
        lot.setNumber(LOT_NUMBER);

        EventType eventType = new EventType();
        eventType.setType("RFI");
        eventType.setAssessmentToolId("FCA_TOOL_1");

        when(agreementService.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenReturn(mockLot);

        Collection<EventType> result = businessLogicClient.getLotEventTypes(AGREEMENT_NUMBER, LOT_NUMBER);

        assertNotNull(result);
    }

    @Test
    void testGetLotEventTypesWithInvalidId() throws Exception {
        when(agreementService.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenThrow(new LotNotFoundException(AGREEMENT_NUMBER, LOT_NUMBER));

        LotNotFoundException thrown = Assertions.assertThrows(
                LotNotFoundException.class,
                () -> businessLogicClient.getLotEventTypes(AGREEMENT_NUMBER, LOT_NUMBER),
                EXCEPTION_FAILED_LOT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testGetEventDataTemplatesWithValidId() throws Exception {
        final LotDetail lot = new LotDetail();
        lot.setNumber(LOT_NUMBER);

        ProcurementDataTemplate dataTemplate = new ProcurementDataTemplate();
        dataTemplate.setCriteria("[{\"id\\\": \\\"Criterion 1\\\"}]");

        when(agreementService.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenReturn(mockLot);
        when(questionTemplateService.getDataTemplates(any(), any())).thenReturn(Collections.singleton(dataTemplate));

        Collection<ProcurementDataTemplate> result = businessLogicClient.getEventDataTemplates(AGREEMENT_NUMBER, LOT_NUMBER, EVENT_TYPE_RFI);

        assertNotNull(result);
    }

    @Test
    void testGetEventDataTemplatesWithInvalidId() throws Exception {
        when(agreementService.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenThrow(new LotNotFoundException(AGREEMENT_NUMBER, LOT_NUMBER));

        LotNotFoundException thrown = Assertions.assertThrows(
                LotNotFoundException.class,
                () -> businessLogicClient.getEventDataTemplates(AGREEMENT_NUMBER, LOT_NUMBER, EVENT_TYPE_RFI),
                EXCEPTION_FAILED_LOT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testGetEventDocumentTemplatesWithValidId() throws Exception {
        final LotDetail lot = new LotDetail();
        lot.setNumber(LOT_NUMBER);

        Document doc = new Document();
        doc.setUrl("http://url");

        when(agreementService.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenReturn(mockLot);
        when(mockLot.getProcurementQuestionTemplates()).thenReturn(lotProcurementQuestionTemplates);

        Collection<Document> result = businessLogicClient.getEventDocumentTemplates(AGREEMENT_NUMBER, LOT_NUMBER, EVENT_TYPE_RFI);

        assertNotNull(result);
    }

    @Test
    void testGetEventDocumentTemplatesWithInvalidId() throws Exception {
        when(agreementService.findLotByAgreementNumberAndLotNumber(AGREEMENT_NUMBER, LOT_NUMBER)).thenThrow(new LotNotFoundException(AGREEMENT_NUMBER, LOT_NUMBER));

        LotNotFoundException thrown = Assertions.assertThrows(
                LotNotFoundException.class,
                () -> businessLogicClient.getEventDocumentTemplates(AGREEMENT_NUMBER, LOT_NUMBER, EVENT_TYPE_RFI),
                EXCEPTION_FAILED_LOT_TEXT
        );

        assertTrue(thrown.getMessage().contains(EXCEPTION_CHECK_TEXT));
    }

    @Test
    void testSaveAgreementWithValidAgreement() throws Exception {
        AgreementDetail ad = new AgreementDetail("Technology Products 2", "Short textual description of the commercial agreement", "CCS", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(5), "URL", true);
        CommercialAgreement ca = new CommercialAgreement("RM1045", "Technology Products 2", "CCS", "Short textual description of the commercial agreement", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(5), "URL", true);

        when(agreementService.createOrUpdateAgreement(ca)).thenReturn(ca);
        AgreementDetail result = businessLogicClient.saveAgreement(ad, "RM1045");

        assertNotNull(result);
    }

    @Test
    void testSaveAgreementWithInvalidAgreement() throws Exception {
        AgreementDetail ad = new AgreementDetail("Technology Products 2", "", "CCS", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(5), "URL", true);

        when(agreementService.createOrUpdateAgreement(mockCommercialAgreement)).thenThrow(new InvalidAgreementException("description"));

        InvalidAgreementException thrown = Assertions.assertThrows(
                InvalidAgreementException.class,
                () -> businessLogicClient.saveAgreement(ad, "RM1045"),
                GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION
        );

        assertTrue(thrown.getMessage().contains("Invalid agreement format, missing 'description'"));
    }

    @Test
    void testSaveLotWithValidLot() throws Exception {
        LotDetail lotDetail = new LotDetail("11", "Lot 11 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);
        Lot lot = new Lot("11", "Lot 11 Name", "Some description", "PRODUCT", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), mockCommercialAgreement);

        when(agreementService.findAgreementByNumber(mockCommercialAgreement.getNumber())).thenReturn(mockCommercialAgreement);
        when(agreementService.createOrUpdateLot(lot)).thenReturn(lot);
        LotDetail result = businessLogicClient.saveLot(lotDetail, mockCommercialAgreement.getNumber(), "11");

        assertNotNull(result);
    }

    @Test
    void testSaveLotWithInvalidLot() throws Exception {
        LotDetail lotDetail = new LotDetail("11", "Lot 11 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);
        lotDetail.setDescription(null);

        when(agreementService.createOrUpdateLot(mockLot)).thenThrow(new InvalidLotException("description", lotDetail.getNumber()));
        when(agreementService.findAgreementByNumber(mockCommercialAgreement.getNumber())).thenReturn(mockCommercialAgreement);

        InvalidLotException thrown = Assertions.assertThrows(
                InvalidLotException.class,
                () -> businessLogicClient.saveLot(lotDetail, mockCommercialAgreement.getNumber(), "11"),
                GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION
        );

        assertTrue(thrown.getMessage().contains("Invalid lot format, missing 'description'"));
    }

    @Test
    void testSaveLotsWithAllValidLots() throws Exception {
        LotDetail lotDetail = new LotDetail("1", "Lot 1 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);
        LotDetail lotDetail1 = new LotDetail("2", "Lot 2 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.SERVICE);

        Lot lot = new Lot("1", "Lot 1 Name", "Some description", "PRODUCT", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), mockCommercialAgreement);
        Lot lot1 = new Lot("2", "Lot 2 Name", "Some description", "SERVICE", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), mockCommercialAgreement);

        Collection<LotDetail> lotDetailSet  = new HashSet<LotDetail>(List.of(lotDetail, lotDetail1));

        Lot lotClass = mock(Lot.class);
        when(agreementService.findAgreementByNumber(mockCommercialAgreement.getNumber())).thenReturn(mockCommercialAgreement);
        doNothing().doNothing().when(lotClass).isValid();
        when(agreementService.createOrUpdateLot(isA(Lot.class))).thenReturn(lot).thenReturn(lot1);


        Collection<LotDetail> result = businessLogicClient.saveLots(lotDetailSet, mockCommercialAgreement.getNumber());
        assertNotNull(result);
    }

    @Test
    void testSaveLotsWithAnInvalidLot() throws Exception {
        LotDetail lotDetail = new LotDetail("1", "Lot 1 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.PRODUCT);
        LotDetail lotDetail1 = new LotDetail("2", "Lot 2 Name", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(2), "Some description",  LotType.SERVICE);

        lotDetail1.setDescription(null);

        Collection<LotDetail> lotDetailSet  = new HashSet<LotDetail>(List.of(lotDetail, lotDetail1));

        Lot lotClass = mock(Lot.class);
        when(agreementService.findAgreementByNumber(mockCommercialAgreement.getNumber())).thenReturn(mockCommercialAgreement);
        doNothing().doThrow(new InvalidLotException("description", lotDetail.getNumber())).when(lotClass).isValid();


        InvalidLotException thrown = Assertions.assertThrows(
                InvalidLotException.class,
                () -> businessLogicClient.saveLots(lotDetailSet, mockCommercialAgreement.getNumber()),
                GlobalErrorHandler.ERR_MSG_VALIDATION_DESCRIPTION
        );

        assertTrue(thrown.getMessage().contains("Invalid lot format, missing 'description' for Lot 2"));
    }
}
