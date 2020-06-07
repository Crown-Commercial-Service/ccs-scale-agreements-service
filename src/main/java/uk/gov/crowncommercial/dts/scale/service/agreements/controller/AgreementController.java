package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.AgreementService;

/**
 * Agreement Controller.
 * 
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AgreementController {

	private final AgreementService service;

	@GetMapping("/agreements")
	public Collection<AgreementSummary> getAgreements() {
		log.debug("getAgreements");
		return service.getAgreements();
	}

	@GetMapping("/agreements/{ca-number}")
	public AgreementDetail getAgreement(@PathVariable(value = "ca-number") String caNumber) {
		log.debug("getAgreement: {}", caNumber);
		return service.getAgreement(caNumber);
	}

	@GetMapping("/agreements/{ca-number}/documents")
	public Collection<Document> getAgreementDocuments(@PathVariable(value = "ca-number") String caNumber) {
		log.debug("getAgreementDocuments: {}", caNumber);
		return service.getAgreementDocuments(caNumber);
	}

	@GetMapping("/agreements/{ca-number}/lots/{lot-number}")
	public LotDetail getLot(@PathVariable(value = "ca-number") String caNumber,
			@PathVariable(value = "lot-number") String lotNumber) {
		log.debug("getLot: caNumber={}, lotNumber={}", caNumber, lotNumber);
		return service.getLot(caNumber, lotNumber);
	}

	@GetMapping("/agreements/{ca-number}/lots/{lot-number}/suppliers")
	public Collection<Organisation> getLotSuppliers(@PathVariable(value = "ca-number") String caNumber,
			@PathVariable(value = "lot-number") String lotNumber) {
		log.debug("getLotSuppliers: caNumber={}, lotNumber={}", caNumber, lotNumber);
		return service.getLotSuppliers(caNumber, lotNumber);
	}

	@GetMapping("/agreements/{ca-number}/lots/{lot-number}/documents")
	public Collection<Document> getLotDocuments(@PathVariable(value = "ca-number") String caNumber,
			@PathVariable(value = "lot-number") String lotNumber) {
		log.debug("getLotDocuments: caNumber={}, lotNumber={}", caNumber, lotNumber);
		return service.getLotDocuments(caNumber, lotNumber);
	}

	@GetMapping("/agreements/{ca-number}/updates")
	public Collection<AgreementUpdate> getAgreementUpdates(@PathVariable(value = "ca-number") String caNumber) {
		log.debug("getAgreementUpdates: {}", caNumber);
		return service.getAgreementUpdates(caNumber);
	}
}
