package uk.gov.crowncommercial.dts.scale.service.agreements.BLL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotOrganisationRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementQuestionTemplate;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.QuestionTemplateService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.WordpressService;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
    private AgreementConverter agreementConverter;

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
        when(agreementConverter.convertAgreementToDTO(any())).thenReturn(agreement);

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

        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
        when(agreementConverter.convertLotsToDTOs(any())).thenReturn(Arrays.asList(lot));

        Collection<LotDetail> result = businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.NONE);

        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void testGetLotsForAgreementWithValidIdAndBuyingMethodWithMatches() throws Exception {
        final RouteToMarketDTO furtherCompetition = new RouteToMarketDTO();
        furtherCompetition.setBuyingMethod(BuyingMethod.E_AUCTION);
        final RouteToMarketDTO directAward = new RouteToMarketDTO();
        directAward.setBuyingMethod(BuyingMethod.DIRECT_AWARD);

        final LotDetail firstLot = new LotDetail();
        firstLot.setNumber(LOT_NUMBER);
        firstLot.setRoutesToMarket(Arrays.asList(furtherCompetition));

        final LotDetail secondLot = new LotDetail();
        secondLot.setNumber(LOT_NUMBER);
        secondLot.setRoutesToMarket(Arrays.asList(directAward));

        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
        when(agreementConverter.convertLotsToDTOs(any())).thenReturn(Arrays.asList(firstLot, secondLot));

        Collection<LotDetail> result = businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.DIRECT_AWARD);

        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void testGetLotsForAgreementWithValidIdAndBuyingMethodWithoutMatches() throws Exception {
        final RouteToMarketDTO furtherCompetition = new RouteToMarketDTO();
        furtherCompetition.setBuyingMethod(BuyingMethod.E_AUCTION);
        final RouteToMarketDTO directAward = new RouteToMarketDTO();
        directAward.setBuyingMethod(BuyingMethod.E_AUCTION);

        final LotDetail firstLot = new LotDetail();
        firstLot.setNumber(LOT_NUMBER);
        firstLot.setRoutesToMarket(Arrays.asList(furtherCompetition));

        final LotDetail secondLot = new LotDetail();
        secondLot.setNumber(LOT_NUMBER);
        secondLot.setRoutesToMarket(Arrays.asList(directAward));

        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
        when(agreementConverter.convertLotsToDTOs(any())).thenReturn(Arrays.asList(firstLot, secondLot));

        Collection<LotDetail> result = businessLogicClient.getLotsForAgreement(AGREEMENT_NUMBER, BuyingMethod.DIRECT_AWARD);

        assertNotNull(result);
        assertEquals(result.size(), 0);
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
        when(agreementConverter.convertAgreementDocumentsToDTOs(any())).thenReturn(Arrays.asList(document));

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
        when(agreementConverter.convertAgreementUpdatesToDTOs(any())).thenReturn(Arrays.asList(update));

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
        when(agreementConverter.convertLotToDTO(any())).thenReturn(lot);

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
        when(agreementConverter.convertLotOrgRolesToLotSupplierDTOs(any())).thenReturn(Collections.singleton(lotSupplier));

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
        when(agreementConverter.convertLotProcurementEventTypesToDTOs(any())).thenReturn(Collections.singleton(eventType));

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
        when(agreementConverter.convertLotProcurementQuestionTemplateToDocumentTemplates(any(), any())).thenReturn(Collections.singleton(doc));

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
}
