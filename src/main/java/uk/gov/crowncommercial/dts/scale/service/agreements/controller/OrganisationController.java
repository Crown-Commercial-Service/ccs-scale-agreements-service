package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.MappingService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.OrganisationService;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.OrganizationDetail;

/**
 * Organisation Controller
 */
@RestController
@RequestMapping("/organisation")
@RequiredArgsConstructor
@Slf4j
public class OrganisationController {

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    protected MappingService mappingService;

    @GetMapping("/{registryCode}")
    public OrganizationDetail getOrganisationDetail(@PathVariable(value = "registryCode") final String registryCode) {
        log.debug("Get Organisation called with registryCode: {}", registryCode);

        Organisation organisation = organisationService.findOrganisationByRegistryCode(registryCode);

        OrganizationDetail model = mappingService.mapOrganisationToOrganizationDetail(organisation);

        return model;
    }

    @PutMapping("/{registryCode}")
    public OrganizationDetail replaceOrganisation(@RequestBody Organisation newOrganisation, @PathVariable String registryCode) {

        Organisation result2 = organisationService.findOrganisationByRegistryCode(registryCode);
        if (result2 != null){
            result2.setLegalName("TEST");
            organisationService.saveAndFlush(result2);
        }
        Organisation result3 = organisationService.findOrganisationByRegistryCode(registryCode);
        return mappingService.mapOrganisationToOrganizationDetail(result3);


//        Organisation result = organisationService.findOrganisationByRegistryCode(registryCode)
//            .map(organisation -> {
//                organisation.setLegalName(newOrganisation.getLegalName());
//                organisation.setIncorporationDate(newOrganisation.getIncorporationDate());
//                organisation.setUri(newOrganisation.getUri());
//                organisation.setIncorporationCountry(newOrganisation.getIncorporationCountry());
//                return organisationService.saveAndFlush(organisation);
//            });
//            .orElseGet(() -> {
//                return organisationService.save(newOrganisation);
//            });

//        return mappingService.mapOrganisationToOrganizationDetail(result);
    }
    //additional methods omitted

}

