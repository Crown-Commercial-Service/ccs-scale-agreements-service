package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

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

/**
 * Mock Implementation of a Repository which just pulls static data from json
 * files for demo purposes.
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementRepositoryMock {

	private final DataLoader loader;

	public Collection<AgreementSummary> getAgreements() {
		log.debug("getAgreements");
		return loader.convertJsonToList("mock-data/agreement-summary-data.json", AgreementSummary.class);
	}

	public AgreementDetail getAgreement(final String caNumber) {
		log.debug("getAgreement: {}", caNumber);
		return loader.convertJsonToObject("mock-data/agreement-detail-data-" + caNumber + ".json",
				AgreementDetail.class);
	}

	public LotDetail getLot(final String caNumber, final String lotNumber) {
		log.debug("getLot: caNumber={}, lotNumber={}", caNumber, lotNumber);
		return loader.convertJsonToObject("mock-data/lot-detail-data-" + caNumber + "-" + lotNumber + ".json",
				LotDetail.class);
	}

	public Collection<AgreementUpdate> getAgreementUpdates(final String caNumber) {
		log.debug("getAgreementUpdates: {}", caNumber);
		return loader.convertJsonToList("mock-data/agreement-updates-data.json", AgreementUpdate.class);
	}

	public Collection<Document> getAgreementDocuments(String caNumber) {
		log.debug("getAgreementDocuments: {}", caNumber);
		return loader.convertJsonToList("mock-data/agreement-documents-data-" + caNumber + ".json", Document.class);
	}

	public Collection<Organisation> getLotSuppliers(String caNumber, String lotNumber) {
		log.debug("getLotSuppliers: caNumber={}, lotNumber={}", caNumber, lotNumber);
		return loader.convertJsonToList("mock-data/lot-suppliers-data-" + caNumber + "-" + lotNumber + ".json",
				Organisation.class);
	}

	public Collection<Document> getLotDocuments(String caNumber, String lotNumber) {
		log.debug("getLotDocuments: caNumber={}, lotNumber={}", caNumber, lotNumber);
		return loader.convertJsonToList("mock-data/lot-documents-data-" + caNumber + "-" + lotNumber + ".json",
				Document.class);
	}

}
