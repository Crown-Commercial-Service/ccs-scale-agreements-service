package uk.gov.crowncommercial.dts.scale.agreements.repository;

import java.util.Collection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.agreements.model.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.agreements.model.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.agreements.model.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.agreements.model.LotDetail;

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

}
