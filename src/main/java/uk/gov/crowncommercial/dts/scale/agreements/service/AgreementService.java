package uk.gov.crowncommercial.dts.scale.agreements.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.agreements.model.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.agreements.model.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.agreements.model.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.agreements.model.LotDetail;
import uk.gov.crowncommercial.dts.scale.agreements.repository.AgreementRepositoryMock;

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

}
