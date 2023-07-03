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
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.WordpressService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    private WordpressService wordpressService;

    @MockBean
    private AgreementConverter agreementConverter;

    @Autowired
    private BusinessLogicClient businessLogicClient;

    private static final String AGREEMENT_NUMBER = "RM3733";
    private static final String LOT_NUMBER = "Lot 1";

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
                "Agreement Not Found exception was not thrown"
        );

        assertTrue(thrown.getMessage().contains("not found"));
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
                "Agreement Not Found exception was not thrown"
        );

        assertTrue(thrown.getMessage().contains("not found"));
    }

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
