package uk.gov.crowncommercial.dts.scale.service.agreements.BLL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.crowncommercial.dts.scale.service.agreements.config.EhcacheConfig;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.WordpressService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private WordpressService wordpressService;

    @Autowired
    private BusinessLogicClient businessLogicClient;

    private static final String AGREEMENT_NUMBER = "RM3733";

    @Test
    void testGetAgreementsList() throws Exception {
        when(agreementService.getAgreements()).thenReturn(Arrays.asList(mockCommercialAgreement));

        List<AgreementSummary> result = businessLogicClient.getAgreementsList();

        assertEquals(result.size(), 1);
    }

    @Test
    void testGetAgreementDetailWithValidId() throws Exception {
        when(agreementService.findAgreementByNumber(AGREEMENT_NUMBER)).thenReturn(mockCommercialAgreement);
        when(wordpressService.getExpandedCommercialAgreement(any(), eq(AGREEMENT_NUMBER))).thenReturn(mockCommercialAgreement);

        AgreementDetail result = businessLogicClient.getAgreementDetail(AGREEMENT_NUMBER);

        assertNotNull(result);
    }

    // getAgreementDetail with invalid ID returns exception

    // getLotsForAgreement with valid ID and no buying method returns model

    // getLotsForAgreement with valid ID and buying method returns filtered populated list

    // getLotsForAgreement with valid ID and buying method returns filtered empty list

    // getLotsForAgreement with invalid ID returns null

    // getDocumentsForAgreement with valid ID returns model

    // getDocumentsForAgreement with invalid ID returns null

    // getUpdatesForAgreement with valid ID returns model

    // getUpdatesForAgreement with invalid ID returns null

    // getLotDetail with valid IDs returns model

    // getLotDetail with invalid IDs returns null

    // getLotSuppliers valid

    // getLotSuppliers invalid

    // getLotEventTypes valid

    // getLotEventTypes invalid

    // getEventDataTemplates valid

    // getEventDataTemplates invalid

    // getEventDocumentTemplates valid

    // getEventDocumentTemplates invalid
}
