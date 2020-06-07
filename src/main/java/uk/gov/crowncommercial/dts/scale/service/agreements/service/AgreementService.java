package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.AgreementRepositoryMock;

/**
 * Agreement Service.
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementService {

	private final AgreementRepositoryMock repository;

	public Collection<AgreementSummary> getAgreements() {
		log.debug("getAgreements");
		return repository.getAgreements();
	}

	public AgreementDetail getAgreement(final String caNumber) {
		log.debug("getAgreement: {}", caNumber);
		return repository.getAgreement(caNumber);
	}

	public LotDetail getLot(final String caNumber, final String lotNumber) {
		log.debug("getLot: {},{}", caNumber, lotNumber);
		return repository.getLot(caNumber, lotNumber);
	}

	public Collection<AgreementUpdate> getAgreementUpdates(final String caNumber) {
		log.debug("getAgreementUpdates: {}", caNumber);
		return repository.getAgreementUpdates(caNumber);
	}

	public Collection<Document> getAgreementDocuments(final String caNumber) {
		log.debug("getAgreementDocuments: {}", caNumber);
		return repository.getAgreementDocuments(caNumber);
	}

	public Collection<Organisation> getLotSuppliers(final String caNumber, final String lotNumber) {
		log.debug("getLotSuppliers: {},{}", caNumber, lotNumber);
		return repository.getLotSuppliers(caNumber, lotNumber);
	}

	public Collection<Document> getLotDocuments(final String caNumber, final String lotNumber) {
		log.debug("getLotDocuments: {},{}", caNumber, lotNumber);
		return repository.getLotDocuments(caNumber, lotNumber);
	}

}
