package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.BuyingMethod;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.RouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotRouteToMarket;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.AgreementRepositoryMock;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.CommercialAgreementRepo;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.LotRepo;

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
	private final LotRepo lotRepo;

	public Collection<AgreementSummary> getAgreements() {
		log.debug("getAgreements");
		List<CommercialAgreement> agreements = commercialAgreementRepo.findAll();
		return agreements.stream().map(ca -> convertToAgreementSummary(ca)).collect(Collectors.toList());
	}

	public AgreementDetail getAgreement(final String caNumber) {
		log.debug("getAgreement: {}", caNumber);
		return convertToAgreementDetail(commercialAgreementRepo.findAgreementByNumber(caNumber));
	}

	public LotDetail getLot(final String caNumber, final String lotNumber) {
		log.debug("getLot: {},{}", caNumber, lotNumber);
		return convertToLotDetail(lotRepo.findLotByNumber(caNumber, lotNumber));
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

	private AgreementDetail convertToAgreementDetail(CommercialAgreement ca) {
		AgreementDetail agreement = new AgreementDetail();
		agreement.setName(ca.getName());
		agreement.setNumber(ca.getNumber());
		agreement.setDescription(ca.getDescription());
		agreement.setStartDate(ca.getStartDate());
		agreement.setEndDate(ca.getEndDate());
		agreement.setDetailUrl(ca.getAgreementUrl());

		agreement.setBenefits(null);
		agreement.setContactDetails(null);
		agreement.setDocuments(null);
		agreement.setLots(null);

		return agreement;
	}

	private LotDetail convertToLotDetail(Lot lot) {
		LotDetail lotDetail = new LotDetail();
		lotDetail.setName(lot.getName());
		lotDetail.setNumber(lot.getNumber());
		lotDetail.setDescription(lot.getDescription());

		// TODO

		lotDetail.setSectors(convertToSectors(lot.getSectors()));
		lotDetail.setRoutesToMarket(convertToRTMs(lot.getRoutesToMarket()));
		return lotDetail;
	}

	private List<String> convertToSectors(Collection<Sector> sectors) {
		List<String> sectorNames = new ArrayList<>();
		for (Sector s : sectors) {
			sectorNames.add(s.getName());
		}
		return sectorNames;
	}

	private Collection<RouteToMarket> convertToRTMs(Collection<LotRouteToMarket> rtms) {
		List<RouteToMarket> routesToMarket = new ArrayList<>();
		for (LotRouteToMarket r : rtms) {
			routesToMarket.add(convertToRTM(r));
		}
		return routesToMarket;
	}

	private RouteToMarket convertToRTM(LotRouteToMarket rtm) {
		RouteToMarket routeToMarket = new RouteToMarket();
		routeToMarket.setBuyingMethod(BuyingMethod.DIRECT_AWARD);
		return routeToMarket;
	}
}
