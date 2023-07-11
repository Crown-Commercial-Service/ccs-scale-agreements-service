package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Note these tests don't cover all aspects of the mapping - they're intended to ensure that mapping functions at a model level, not to cover every possible mapping option
 */
@SpringBootTest
@ActiveProfiles("test")
public class MapStructMappingTests {
    private AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);
    private AgreementDetailMapper agreementDetailMapper = Mappers.getMapper(AgreementDetailMapper.class);
    private LotDetailMapper lotDetailMapper = Mappers.getMapper(LotDetailMapper.class);
    private DocumentMapper documentMapper = Mappers.getMapper(DocumentMapper.class);
    private AgreementUpdateMapper updateMapper = Mappers.getMapper(AgreementUpdateMapper.class);
    private LotSupplierMapper supplierMapper = Mappers.getMapper(LotSupplierMapper.class);
    private EventTypeMapper eventTypeMapper = Mappers.getMapper(EventTypeMapper.class);

    private static final String AGREEMENT_NAME = "Agreement Name";
    private static final String AGREEMENT_NUMBER = "RM1234";
    private static final String LOT_NUMBER = "Lot 1";
    private static final String LOT_NAME = "Name of Lot";
    private static final String DOCUMENT_TYPE = "Document Type";
    private static final String DOCUMENT_NAME = "Document Name";
    private static final String DOCUMENT_URL = "http://document";
    private static final String DOCUMENT_FORMAT = "text/html";
    private static final String DOCUMENT_LANGUAGE = "en_GB";
    private static final String DOCUMENT_DESCRIPTION = "Document Description";
    private static final Integer DOCUMENT_VERSION = 1;
    private static final Timestamp DOCUMENT_PUBLISHED_DATE = new Timestamp(System.currentTimeMillis());
    private static final Timestamp DOCUMENT_MODIFIED_DATE = new Timestamp(System.currentTimeMillis());
    private static final String UPDATE_DESCRIPTION = "Update Description";
    private static final LocalDate UPDATE_PUBLISHED_DATE = LocalDate.now();
    private static final Timestamp UPDATE_PUBLISHED_DATE_TS = Timestamp.from(Instant.from(UPDATE_PUBLISHED_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    private static final String UPDATE_URL = "http://update";
    private static final String ORG_ENTITY_ID = "%d";
    private static final String ORG_LEGAL_NAME = "ACME Trading Ltd %d";
    private static final String ORG_URI = "https://www.acmetrading%d.com";
    private static final String CONTACT_NAME = "Contact %d";
    private static final String CONTACT_REASON = "Contact reason %d";
    public static final String EXPRESSION_OF_INTEREST = "Expression Of Interest";
    public static final String EOI = "EOI";
    public static final boolean IS_PRE_MARKET_ACTIVITY = false;
    public static final String ASSESSMENT_TOOL_ID = "FCA_TOOL_1";

    @Test
    public void testCommercialAgreementMapsToAgreementSummary() throws Exception {
        CommercialAgreement sourceModel = new CommercialAgreement();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);

        AgreementSummary outputModel = agreementSummaryMapper.commercialAgreementToAgreementSummary(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }

    @Test
    public void testCommercialAgreementMapsToAgreementDetail() throws Exception {
        CommercialAgreement sourceModel = new CommercialAgreement();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);

        AgreementDetail outputModel = agreementDetailMapper.commercialAgreementToAgreementDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }

    @Test
    public void testLotMapsToLotDetail() throws Exception {
        Lot sourceModel = new Lot();
        sourceModel.setNumber(LOT_NUMBER);
        sourceModel.setName(LOT_NAME);

        LotDetail outputModel = lotDetailMapper.lotToLotDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
    }

    @Test
    public void testCommercialAgreementDocumentMapsToDocument() throws Exception {
        CommercialAgreementDocument document = new CommercialAgreementDocument();
        document.setDocumentType(DOCUMENT_TYPE);
        document.setName(DOCUMENT_NAME);
        document.setUrl(DOCUMENT_URL);
        document.setVersion(DOCUMENT_VERSION);
        document.setFormat(DOCUMENT_FORMAT);
        document.setLanguage(DOCUMENT_LANGUAGE);
        document.setDescription(DOCUMENT_DESCRIPTION);
        document.setPublishedDate(DOCUMENT_PUBLISHED_DATE);
        document.setModifiedDate(DOCUMENT_MODIFIED_DATE);

        Document outputModel = documentMapper.commercialAgreementDocumentToDocument(document);

        assertNotNull(outputModel);
        assertEquals(document.getName(), outputModel.getName());
        assertEquals(document.getDocumentType(), outputModel.getDocumentType());
        assertEquals(document.getUrl(), outputModel.getUrl());
        assertEquals(document.getFormat(), outputModel.getFormat());
        assertEquals(document.getLanguage(), outputModel.getLanguage());
        assertEquals(document.getDescription(), outputModel.getDescription());
    }

    @Test
    public void testCommercialAgreementUpdateMapsToAgreementUpdate() throws Exception {
        CommercialAgreementUpdate update = new CommercialAgreementUpdate();
        update.setDescription(UPDATE_DESCRIPTION);
        update.setPublishedDate(UPDATE_PUBLISHED_DATE_TS);
        update.setUrl(UPDATE_URL);

        AgreementUpdate outputModel = updateMapper.commercialAgreementUpdateToAgreementUpdate(update);

        assertNotNull(outputModel);
        assertEquals(update.getDescription(), outputModel.getText());
        assertEquals(update.getDescription(), outputModel.getText());
        assertEquals(update.getPublishedDate().toLocalDateTime().toLocalDate(), outputModel.getDate());
        assertEquals(update.getUrl(), outputModel.getLinkUrl());
    }

    @Test
    public void testLotOrganisationRoleMapsToLotSupplier() throws Exception {
        Set<ContactPointLotOrgRole> cpLotOrgRoles = new HashSet<>();

        ContactPointLotOrgRole cpLotOrgRole = new ContactPointLotOrgRole();

        ContactPointReason cpReason = new ContactPointReason();
        cpReason.setName(format(CONTACT_REASON, 1));
        cpLotOrgRole.setContactPointName(format(CONTACT_NAME, 1));
        cpLotOrgRole.setContactPointReason(cpReason);
        cpLotOrgRole.setPrimary(true);
        cpLotOrgRoles.add(cpLotOrgRole);

        Organisation org = new Organisation();
        org.setEntityId(format(ORG_ENTITY_ID, 1));
        org.setLegalName(format(ORG_LEGAL_NAME, 1));
        org.setUri(format(ORG_URI, 1));
        org.setRegistryCode("GB-COH");

        org.setIncorporationDate(LocalDate.of(1, 1, 1));
        org.setIncorporationCountry("GB");
        org.setIsSme(true);
        org.setIsVcse(true);
        org.setStatus("active");
        org.setIsActive(true);

        LotOrganisationRole lotOrgRole = new LotOrganisationRole();
        lotOrgRole.setContactPointLotOrgRoles(cpLotOrgRoles);
        lotOrgRole.setOrganisation(org);

        LotSupplier outputModel = supplierMapper.lotOrganisationRoleToLotSupplier(lotOrgRole);

        assertNotNull(outputModel);
        assertNotNull(outputModel.getOrganization());
        assertNotNull(outputModel.getLotContacts());
        assertNotNull(outputModel.getSupplierStatus());
    }

    @Test
    public void testLotProcurementEventTypeMapsToEventType() throws Exception {
        Lot sourceModel = new Lot();
        sourceModel.setNumber(LOT_NUMBER);
        sourceModel.setName(LOT_NAME);

        ProcurementEventType procurementEventType = new ProcurementEventType();
        procurementEventType.setName(EOI);
        procurementEventType.setDescription(EXPRESSION_OF_INTEREST);
        procurementEventType.setPreMarketActivity(IS_PRE_MARKET_ACTIVITY);

        LotProcurementEventType lotEventType = new LotProcurementEventType();
        lotEventType.setProcurementEventType(procurementEventType);
        lotEventType.setAssessmentToolId("FCA_TOOL_1");

        EventType outputModel = eventTypeMapper.lotProcurementEventTypeToEventType(lotEventType, sourceModel);

        assertNotNull(outputModel);
        assertEquals(EOI, outputModel.getType());
        assertEquals(EXPRESSION_OF_INTEREST, outputModel.getDescription());
        assertEquals(IS_PRE_MARKET_ACTIVITY, outputModel.getPreMarketActivity());
        assertEquals(ASSESSMENT_TOOL_ID, outputModel.getAssessmentToolId());
    }
}
