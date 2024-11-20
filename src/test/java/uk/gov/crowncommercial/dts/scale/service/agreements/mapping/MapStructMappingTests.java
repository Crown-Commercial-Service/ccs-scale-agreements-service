package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidAgreementTypeException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.InvalidRegulationException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Note these tests don't cover all aspects of the mapping - they're intended to ensure that mapping functions at a model level, not to cover every possible mapping option
 */
@SpringBootTest
@ActiveProfiles("test")
public class MapStructMappingTests {
    private final AgreementSummaryMapper agreementSummaryMapper = Mappers.getMapper(AgreementSummaryMapper.class);
    private final AgreementDetailMapper agreementDetailMapper = Mappers.getMapper(AgreementDetailMapper.class);
    private final LotDetailMapper lotDetailMapper = Mappers.getMapper(LotDetailMapper.class);
    private final DocumentMapper documentMapper = Mappers.getMapper(DocumentMapper.class);
    private final AgreementUpdateMapper updateMapper = Mappers.getMapper(AgreementUpdateMapper.class);
    private final LotSupplierMapper supplierMapper = Mappers.getMapper(LotSupplierMapper.class);
    private final EventTypeMapper eventTypeMapper = Mappers.getMapper(EventTypeMapper.class);
    private final ProcurementDataTemplateMapper dataTemplateMapper = Mappers.getMapper(ProcurementDataTemplateMapper.class);
    private final QuestionTemplateMapper questionTemplateMapper = Mappers.getMapper(QuestionTemplateMapper.class);

    private static final String AGREEMENT_NAME = "Agreement Name";
    private static final String AGREEMENT_NUMBER = "RM1234";
    private static final String AGREEMENT_DESCRIPTION = "This is an agreement description.";
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
    public static final Integer TEMPLATE_ID = 1;
    public static final String TEMPLATE_NAME = "Template name";
    public static final Boolean TEMPLATE_MANDATORY = true;
    public static final String TEMPLATE_URL = "http://www.google.com";
    public static final String INVALID = "SOMETHING_INVALID";

    private static final String INVALID_REGULATION_MESSAGE = "Invalid regulation '%s' passed in. Regulation must be one of the following: [PA2023, PCR2015, PCR2006]";
    private static final String INVALID_AGREEMENT_TYPE_MESSAGE = "Invalid agreementType '%s' passed in. agreementType must be one of the following: [Dynamic Purchasing System, Dynamic Market, Open Framework, Closed Framework, PCR15 Framework, PCR06 Framework]";
    private static final String INCOMPATIBLE_AGREEMENT_TYPE_MESSAGE = "agreementType must be one of the following: '%s' for '%s'";

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
        assertNull(outputModel.getRegulation());
        assertNull(outputModel.getAgreementType());
    }

    @Test
    public void testCommercialAgreementMapsToAgreementDetailWithRegulation() throws Exception {
        CommercialAgreement sourceModel = new CommercialAgreement();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setRegulation("PA2023");

        AgreementDetail outputModel = agreementDetailMapper.commercialAgreementToAgreementDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
        assertTrue(sourceModel.getRegulation().equalsIgnoreCase(outputModel.getRegulation().getName()));
    }

    @Test
    public void testCommercialAgreementMapsToAgreementDetailWithAgreementType() throws Exception {
        CommercialAgreement sourceModel = new CommercialAgreement();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setRegulation("PA2023");
        sourceModel.setAgreementType("DYNAMIC_MARKET");

        AgreementDetail outputModel = agreementDetailMapper.commercialAgreementToAgreementDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
        assertTrue(sourceModel.getRegulation().equalsIgnoreCase(outputModel.getRegulation().getName()));
        assertTrue(sourceModel.getAgreementType().equalsIgnoreCase(outputModel.getAgreementType().toString()));
    }

    @Test
    public void testAgreementDetailMapsToCommercialAgreement() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setDescription(AGREEMENT_DESCRIPTION);
        sourceModel.setOwnerName("CCS");
        sourceModel.setStartDate(java.time.LocalDate.now());
        sourceModel.setEndDate(java.time.LocalDate.now().plusDays(5));
        sourceModel.setPreDefinedLotRequired(true);
        String[] benefitArray = new String[]{"Benefit 1", "Benefit 2"};
        sourceModel.setBenefits(Arrays.asList(benefitArray));

        CommercialAgreement outputModel = agreementDetailMapper.agreementDetailToCommercialAgreement(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
        assertEquals(sourceModel.getDescription(), outputModel.getDescription());
        assertEquals(sourceModel.getOwnerName(), outputModel.getOwner());
        assertEquals(sourceModel.getStartDate(), outputModel.getStartDate());
        assertEquals(sourceModel.getEndDate(), outputModel.getEndDate());
        assertEquals(sourceModel.getPreDefinedLotRequired(), outputModel.getPreDefinedLotRequired());
        assertNull(outputModel.getRegulation());
        assertNull(outputModel.getAgreementType());

        int index = 0;
        for (CommercialAgreementBenefit benefit : outputModel.getBenefits()) {
            assertEquals(benefitArray[index], benefit.getName());
            index++;
        }
    }

    @Test
    public void testAgreementDetailMapsToCommercialAgreementWithRegulation() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setDescription(AGREEMENT_DESCRIPTION);
        sourceModel.setOwnerName("CCS");
        sourceModel.setStartDate(java.time.LocalDate.now());
        sourceModel.setEndDate(java.time.LocalDate.now().plusDays(5));
        sourceModel.setPreDefinedLotRequired(true);
        sourceModel.setRegulation("PCR2006");

        CommercialAgreement outputModel = agreementDetailMapper.agreementDetailToCommercialAgreement(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
        assertEquals(sourceModel.getDescription(), outputModel.getDescription());
        assertEquals(sourceModel.getOwnerName(), outputModel.getOwner());
        assertEquals(sourceModel.getStartDate(), outputModel.getStartDate());
        assertEquals(sourceModel.getEndDate(), outputModel.getEndDate());
        assertEquals(sourceModel.getPreDefinedLotRequired(), outputModel.getPreDefinedLotRequired());
        assertTrue(outputModel.getRegulation().equalsIgnoreCase(sourceModel.getRegulation().getName()));
    }

    @Test
    public void testAgreementDetailMapsToCommercialAgreementWithAgreementType() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setDescription(AGREEMENT_DESCRIPTION);
        sourceModel.setOwnerName("CCS");
        sourceModel.setStartDate(java.time.LocalDate.now());
        sourceModel.setEndDate(java.time.LocalDate.now().plusDays(5));
        sourceModel.setPreDefinedLotRequired(true);
        sourceModel.setRegulation("PCR2006");
        sourceModel.setAgreementType("PCR06 Framework");

        CommercialAgreement outputModel = agreementDetailMapper.agreementDetailToCommercialAgreement(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
        assertEquals(sourceModel.getDescription(), outputModel.getDescription());
        assertEquals(sourceModel.getOwnerName(), outputModel.getOwner());
        assertEquals(sourceModel.getStartDate(), outputModel.getStartDate());
        assertEquals(sourceModel.getEndDate(), outputModel.getEndDate());
        assertEquals(sourceModel.getPreDefinedLotRequired(), outputModel.getPreDefinedLotRequired());
        assertTrue(outputModel.getRegulation().equalsIgnoreCase(sourceModel.getRegulation().getName()));
        assertTrue(outputModel.getAgreementType().equalsIgnoreCase(sourceModel.getAgreementType().toString()));
    }

    @Test
    public void testAgreementDetailMapsToCommercialAgreementWithInvalidRegulation() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        
        String InvalidRegulation = INVALID;

        Exception exception = assertThrows(InvalidRegulationException.class, () -> {
            sourceModel.setRegulation(InvalidRegulation);
        });

        assertEquals(exception.getMessage(), String.format(INVALID_REGULATION_MESSAGE, InvalidRegulation));
    }

    @Test
    public void testAgreementDetailMapsToCommercialAgreementWithInvalidAgreementType() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setRegulation("PCR2006");

        String InvalidAgreementType = INVALID;

        Exception exception = assertThrows(InvalidAgreementTypeException.class, () -> {
            sourceModel.setAgreementType(InvalidAgreementType);
        });

        assertEquals(exception.getMessage(), String.format(INVALID_AGREEMENT_TYPE_MESSAGE, InvalidAgreementType));
    }

    @Test
    public void testAgreementDetailMapsToCommercialAgreementWithOnlyAgreementType() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setAgreementType("Dynamic Purchasing System");

        CommercialAgreement outputModel = agreementDetailMapper.agreementDetailToCommercialAgreement(sourceModel);

        assertNotNull(outputModel);
        assertNull(outputModel.getRegulation());
        assertNull(outputModel.getAgreementType());
    }


    @Test
    public void testAgreementDetailMapsToCommercialAgreementWithIncompatibleAgreementType_PCR2006() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        String regulation = "PCR2006";
        
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setRegulation(regulation);

        Exception exception = assertThrows(InvalidAgreementTypeException.class, () -> {
            sourceModel.setAgreementType("Dynamic Purchasing System");
        });

        assertEquals(exception.getMessage(), String.format(INCOMPATIBLE_AGREEMENT_TYPE_MESSAGE, "[PCR06 Framework]", regulation));
    }

    @Test
    public void testAgreementDetailMapsToCommercialAgreementWithIncompatibleAgreementType_PCR2015() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        String regulation = "PCR2015";
        
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setRegulation(regulation);

        Exception exception = assertThrows(InvalidAgreementTypeException.class, () -> {
            sourceModel.setAgreementType("Open Framework");
        });

        assertEquals(exception.getMessage(), String.format(INCOMPATIBLE_AGREEMENT_TYPE_MESSAGE, "[Dynamic Purchasing System, PCR15 Framework]", regulation));
    }

    @Test
    public void testAgreementDetailMapsToCommercialAgreementWithIncompatibleAgreementType_PA2023() throws Exception {
        AgreementDetail sourceModel = new AgreementDetail();
        String regulation = "PA2023";
        
        sourceModel.setName(AGREEMENT_NAME);
        sourceModel.setNumber(AGREEMENT_NUMBER);
        sourceModel.setRegulation(regulation);

        Exception exception = assertThrows(InvalidAgreementTypeException.class, () -> {
            sourceModel.setAgreementType("Dynamic Purchasing System");
        });

        assertEquals(exception.getMessage(), String.format(INCOMPATIBLE_AGREEMENT_TYPE_MESSAGE, "[Dynamic Market, Open Framework, Closed Framework]", regulation));
    }

    @Test
    public void testLotMapsToLotDetail() throws Exception {
        Lot sourceModel = new Lot();
        sourceModel.setNumber(LOT_NUMBER);
        sourceModel.setName(LOT_NAME);
        sourceModel.setLotType("service");

        LotDetail outputModel = lotDetailMapper.lotToLotDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
        assertEquals(LotType.SERVICE.getName(), outputModel.getType().getName());

    }

    @Test
    public void testLotDetailMapsToLot() throws Exception {
        LotDetail sourceModel = new LotDetail();
        sourceModel.setNumber(LOT_NUMBER);
        sourceModel.setName(LOT_NAME);
        sourceModel.setStartDate(java.time.LocalDate.now());
        sourceModel.setEndDate(java.time.LocalDate.now().plusDays(5));
        sourceModel.setType(LotType.PRODUCT);


        Lot outputModel = lotDetailMapper.lotDetailToLot(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getName(), outputModel.getName());
        assertEquals(sourceModel.getNumber(), outputModel.getNumber());
        assertEquals(sourceModel.getStartDate(), outputModel.getStartDate());
        assertEquals(sourceModel.getEndDate(), outputModel.getEndDate());
        assertEquals(LotType.PRODUCT.getName(), outputModel.getLotType().toLowerCase());
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

    @Test
    public void testProcurementQuestionTemplateMapsToProcurementDataTemplate() throws Exception {
        ProcurementQuestionTemplate sourceModel = new ProcurementQuestionTemplate();
        sourceModel.setId(TEMPLATE_ID);
        sourceModel.setTemplateName(TEMPLATE_NAME);
        sourceModel.setMandatory(TEMPLATE_MANDATORY);

        ProcurementDataTemplate outputModel = dataTemplateMapper.procurementQuestionTemplateToProcurementDataTemplate(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getId(), outputModel.getId());
        assertEquals(sourceModel.getTemplateName(), outputModel.getTemplateName());
        assertEquals(sourceModel.getMandatory(), outputModel.getMandatory());
    }

    @Test
    public void testLotProcurementQuestionTemplateMapsToDocument() throws Exception {
        ProcurementQuestionTemplate sourceModel = new ProcurementQuestionTemplate();
        sourceModel.setId(TEMPLATE_ID);
        sourceModel.setTemplateName(TEMPLATE_NAME);
        sourceModel.setTemplateUrl(TEMPLATE_URL);

        Document outputModel = documentMapper.procurementQuestionTemplateToDocument(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getTemplateUrl(), outputModel.getUrl());
    }

    @Test
    public void testProcurementQuestionTemplateMapsToQuestionTemplate() throws Exception {
        ProcurementQuestionTemplate sourceModel = new ProcurementQuestionTemplate();
        sourceModel.setId(TEMPLATE_ID);
        sourceModel.setTemplateName(TEMPLATE_NAME);
        sourceModel.setMandatory(true);
        Integer groupId = 10;

        QuestionTemplate outputModel = questionTemplateMapper.procurementQuestionTemplateToQuestionTemplate(sourceModel, groupId);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getId(), outputModel.getTemplateId());
        assertEquals(groupId, outputModel.getTemplateGroupId());
        assertEquals(sourceModel.getTemplateName(), outputModel.getName());
    }

    @Test
    public void testLotSupplierToOrganisationWithValidScheme() throws Exception {
        OrganizationIdentifier oi = new OrganizationIdentifier();
        oi.setLegalName("First company ever");
        oi.setScheme(Scheme.GBCHC);
        oi.setId("1234567");

        OrganizationDetail od = new OrganizationDetail();
        od.setActive(true);
        od.setCountryCode("GB");
        od.setCreationDate(LocalDate.now());

        Organization org = new Organization();
        org.setIdentifier(oi);
        org.setDetails(od);

        LotSupplier sourceModel = new LotSupplier();
        sourceModel.setOrganization(org);

        Organisation outputModel = supplierMapper.lotSupplierToOrganisation(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getOrganization().getIdentifier().getLegalName(), outputModel.getLegalName());
        assertEquals(sourceModel.getOrganization().getIdentifier().getId(), outputModel.getEntityId());
        assertEquals(sourceModel.getOrganization().getIdentifier().getScheme().getName(), outputModel.getRegistryCode());
        assertEquals(sourceModel.getOrganization().getDetails().getCountryCode(), outputModel.getIncorporationCountry());
        assertEquals(sourceModel.getOrganization().getDetails().getActive(), outputModel.getIsActive());
    }

    @Test
    public void testLotSupplierToContactDetail() throws Exception {
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
        org.setAddress(add);
        org.setContactPoint(cp);

        LotSupplier sourceModel = new LotSupplier();
        sourceModel.setOrganization(org);


        ContactDetail outputModel = supplierMapper.lotSupplierToContactDetail(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getOrganization().getAddress().getStreetAddress(), outputModel.getStreetAddress());
        assertEquals(sourceModel.getOrganization().getAddress().getPostalCode(), outputModel.getPostalCode());
        assertEquals(sourceModel.getOrganization().getContactPoint().getName(), outputModel.getName());
        assertEquals(sourceModel.getOrganization().getContactPoint().getEmail(), outputModel.getEmailAddress());
        assertEquals(sourceModel.getOrganization().getContactPoint().getUrl(), outputModel.getUrl());
    }

    @Test
    public void testOrganisationToOrganizationIdentifier() throws Exception {
        Organisation sourceModel = new Organisation();
        sourceModel.setLegalName(format(ORG_LEGAL_NAME, 1));
        sourceModel.setEntityId(format(ORG_ENTITY_ID, 1));
        sourceModel.setRegistryCode("GB-COH");

        OrganizationIdentifier outputModel = supplierMapper.OrganisationToOrganizationIdentifier(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getLegalName(), outputModel.getLegalName());
        assertEquals(sourceModel.getEntityId(), outputModel.getId());
        assertEquals(sourceModel.getRegistryCode(), outputModel.getScheme().getName());
    }

    @Test
    public void testOrganisationToOrganizationIdentifierWithoutScheme() throws Exception {
        Organisation sourceModel = new Organisation();
        sourceModel.setLegalName(format(ORG_LEGAL_NAME, 1));
        sourceModel.setEntityId(format(ORG_ENTITY_ID, 1));

        OrganizationIdentifier outputModel = supplierMapper.OrganisationToOrganizationIdentifier(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getLegalName(), outputModel.getLegalName());
        assertEquals(sourceModel.getEntityId(), outputModel.getId());
        assertEquals(sourceModel.getRegistryCode(), null);
    }

    @Test
    public void testOrganizationIdentifierToOrganisation() throws Exception {

        OrganizationIdentifier sourceModel = new OrganizationIdentifier();
        sourceModel.setLegalName(format(ORG_LEGAL_NAME, 1));
        sourceModel.setId(format(ORG_ENTITY_ID, 1));
        sourceModel.setScheme(Scheme.GBCHC);

        Organisation outputModel = supplierMapper.OrganizationIdentifierToOrganisation(sourceModel);

        assertNotNull(outputModel);
        assertEquals(sourceModel.getLegalName(), outputModel.getLegalName());
        assertEquals(sourceModel.getId(), outputModel.getEntityId());
        assertEquals(sourceModel.getScheme().getName(), outputModel.getRegistryCode());
    }
}
