package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementSummary;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.AgreementUpdate;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Document;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.LotDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.CommercialAgreement;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Lot;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Sector;
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

    List<CommercialAgreement> agreements = service.getAgreements();
    ModelMapper modelMapper = new ModelMapper();
    return agreements.stream().map(ca -> modelMapper.map(ca, AgreementSummary.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/agreements/{ca-number}")
  public AgreementDetail getAgreement(@PathVariable(value = "ca-number") String caNumber) {
    log.debug("getAgreement: {}", caNumber);

    CommercialAgreement ca = service.getAgreement(caNumber);
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(ca, AgreementDetail.class);
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}")
  public LotDetail getLot(@PathVariable(value = "ca-number") String caNumber,
      @PathVariable(value = "lot-number") String lotNumber) {
    log.debug("getLot: caNumber={}, lotNumber={}", caNumber, lotNumber);

    Converter<Sector, String> sectorToStringConverter = new AbstractConverter<Sector, String>() {
      @Override
      protected String convert(Sector source) {
        return source == null ? null : source.getName();
      }
    };
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.addConverter(sectorToStringConverter);
    Lot lot = service.getLot(caNumber, lotNumber);
    return modelMapper.map(lot, LotDetail.class);

  }

  @GetMapping("/agreements/{ca-number}/documents")
  public Collection<Document> getAgreementDocuments(
      @PathVariable(value = "ca-number") String caNumber) {
    log.debug("getAgreementDocuments: {}", caNumber);

    // Not currently implemented in data model
    return new ArrayList<>();
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}/suppliers")
  public Collection<Organisation> getLotSuppliers(
      @PathVariable(value = "ca-number") String caNumber,
      @PathVariable(value = "lot-number") String lotNumber) {
    log.debug("getLotSuppliers: caNumber={}, lotNumber={}", caNumber, lotNumber);

    // Not currently implemented in data model
    return new ArrayList<>();
  }

  @GetMapping("/agreements/{ca-number}/lots/{lot-number}/documents")
  public Collection<Document> getLotDocuments(@PathVariable(value = "ca-number") String caNumber,
      @PathVariable(value = "lot-number") String lotNumber) {
    log.debug("getLotDocuments: caNumber={}, lotNumber={}", caNumber, lotNumber);

    // Not currently implemented in data model
    return new ArrayList<>();
  }

  @GetMapping("/agreements/{ca-number}/updates")
  public Collection<AgreementUpdate> getAgreementUpdates(
      @PathVariable(value = "ca-number") String caNumber) {
    log.debug("getAgreementUpdates: {}", caNumber);

    // Not currently implemented in data model
    return new ArrayList<>();
  }
}
