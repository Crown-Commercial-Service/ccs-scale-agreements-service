package uk.gov.crowncommercial.dts.scale.service.agreements.mapping;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementDocument;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

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
}
