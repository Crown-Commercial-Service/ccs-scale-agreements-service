package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.converter.AgreementConverter;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Agreement Controller.
 *
 */
@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
@Slf4j
public class AgreementController {
    @Autowired
    private BusinessLogicClient businessLogicClient;

    private final AgreementService service;
    private final AgreementConverter converter;

    @GetMapping
    public Collection<AgreementSummary> getAgreements() {
        log.debug("getAgreements called");

        Collection<AgreementSummary> model = businessLogicClient.getAgreementsList();

        return model;
    }

    @GetMapping("/{agreement-id}")
    public AgreementDetail getAgreementDetail(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreement called with ID: {}", agreementNumber);

        AgreementDetail model = businessLogicClient.getAgreementDetail(agreementNumber);

        return model;
    }

    @GetMapping("/{agreement-id}/lots")
    public Collection<LotDetail> getAgreementLots(@PathVariable(value = "agreement-id") final String agreementNumber, @RequestParam Optional<BuyingMethod> buyingMethod) {
        log.debug("getAgreementLots: agreementNumber={}, buyingMethod={}", agreementNumber, buyingMethod);
        final CommercialAgreement ca = getAgreement(agreementNumber);
        Collection<LotDetail> lots = converter.convertLotsToDTOs(ca.getLots());
        if (buyingMethod.isPresent()) {
            return filterLotsByBuyingMethod(lots, buyingMethod.get());
        } else {
            return lots;
        }
    }

    @GetMapping("/{agreement-id}/documents")
    public Collection<Document> getAgreementDocuments(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreementDocuments: {}", agreementNumber);
        final CommercialAgreement ca = getAgreement(agreementNumber);
        return converter.convertAgreementDocumentsToDTOs(ca.getDocuments());
    }

    @GetMapping("/{agreement-id}/updates")
    public Collection<AgreementUpdate> getAgreementUpdates(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreementUpdates: {}", agreementNumber);
        final CommercialAgreement ca = getAgreement(agreementNumber);
        return converter.convertAgreementUpdatesToDTOs(ca.getUpdates());
    }

    private Collection<LotDetail> filterLotsByBuyingMethod(Collection<LotDetail> lots, BuyingMethod buyingMethod) {
        return lots.stream()
            .filter(ld -> ld.getRoutesToMarket().stream()
            .filter(rtm -> buyingMethod == rtm.getBuyingMethod()).count() > 0)
            .collect(Collectors.toSet());
    }
}
