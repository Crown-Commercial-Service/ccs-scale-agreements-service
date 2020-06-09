package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.AgreementRepositoryMock;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementRepo;

/**
 * Agreement Service.
 * 
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementService {

	private final AgreementRepositoryMock repository;
	private final CommercialAgreementRepo commercialAgreementRepo;

	public Collection<AgreementSummary> getAgreements() {
		log.debug("getAgreements");

		List<CommercialAgreement> agreements = commercialAgreementRepo.findAll();

		// TODO: streamy stuff
		List<AgreementSummary> summaries = new ArrayList<>();

		for (CommercialAgreement ca : agreements) {
			summaries.add(convertToAgreementSummary(ca));
		}

		return summaries;
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

	private AgreementSummary convertToAgreementSummary(CommercialAgreement ca) {
		AgreementSummary summary = new AgreementSummary();
		summary.setName(ca.getName());
		summary.setNumber(ca.getNumber());
		return summary;
	}
}
