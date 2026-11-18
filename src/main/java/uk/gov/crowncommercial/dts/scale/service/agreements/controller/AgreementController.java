package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.BLL.BusinessLogicClient;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.*;

/**
 * Agreement Controller
 */
@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
@Slf4j
public class AgreementController {
    @Autowired
    private BusinessLogicClient businessLogicClient;

    private static final Pattern AGREEMENT_ID_PATTERN = Pattern.compile("^(RM[0-9]{1,10}(?:\\.[0-9]{1,5})?)$");

    @GetMapping(value={"", "/"})
    public Collection<AgreementSummary> getAgreements() {
        log.debug("getAgreements called");

        Collection<AgreementSummary> model = businessLogicClient.getAgreementsList();

        return model;
    }

    @GetMapping("/{agreement-id}")
    public AgreementDetail getAgreementDetail(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreement called with ID: {}", agreementNumber);

        // First check if the Agreement ID passed to us is in a valid format
        if (agreementNumber == null || !AGREEMENT_ID_PATTERN.matcher(agreementNumber).matches()) {
            throw new AgreementNotFoundException(agreementNumber);
        }

        // It is valid, so proceed with lookup
        AgreementDetail model = businessLogicClient.getAgreementDetail(agreementNumber);

        return model;
    }

    @GetMapping("/{agreement-id}/lots")
    public Collection<LotDetail> getAgreementLots(@PathVariable(value = "agreement-id") final String agreementNumber, @RequestParam Optional<BuyingMethod> buyingMethod) {
        log.debug("getAgreementLots called: agreementNumber={}, buyingMethod={}", agreementNumber, buyingMethod);
        Collection<LotDetail> model = null;

        if (buyingMethod.isPresent()) {
            model = businessLogicClient.getLotsForAgreement(agreementNumber, buyingMethod.get());
        } else {
            model = businessLogicClient.getLotsForAgreement(agreementNumber, BuyingMethod.NONE);
        }

        return model;
    }

    @GetMapping("/{agreement-id}/documents")
    public Collection<Document> getAgreementDocuments(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreementDocuments called with ID: {}", agreementNumber);

        Collection<Document> model = businessLogicClient.getDocumentsForAgreement(agreementNumber);

        return model;
    }

    @GetMapping("/{agreement-id}/updates")
    public Collection<AgreementUpdate> getAgreementUpdates(@PathVariable(value = "agreement-id") final String agreementNumber) {
        log.debug("getAgreementUpdates called with ID: {}", agreementNumber);

        Collection<AgreementUpdate> model = businessLogicClient.getUpdatesForAgreement(agreementNumber);

        return model;
    }

    @PutMapping("/{agreement-id}")
    public AgreementDetail updateAgreementDetail(@PathVariable(value = "agreement-id") final String agreementNumber, @RequestBody AgreementDetail agreementDetail) {
        log.debug("updateAgreementDetail called with ID: {}", agreementNumber);

        return businessLogicClient.saveAgreement(agreementDetail, agreementNumber);
    }

    @PutMapping("/{agreement-id}/lots")
    public Collection<LotDetail> updateLots(@PathVariable(value = "agreement-id") final String agreementNumber, @RequestBody final Set<LotDetail> lotDetailSet) {
        log.debug("updateLots called with ID: {}", agreementNumber);

        return businessLogicClient.saveLots(lotDetailSet, agreementNumber);
    }

    @PutMapping("/data-templates")
    public ProcurementDataTemplate updateDataTemplates(@RequestBody final ProcurementDataTemplate updatePayload) {
        log.debug("updateDataTemplates called for procurement question template id: {}", updatePayload.getId());

        ProcurementDataTemplate model = businessLogicClient.updateEventDataTemplates(updatePayload);

        return model;
    }
}
