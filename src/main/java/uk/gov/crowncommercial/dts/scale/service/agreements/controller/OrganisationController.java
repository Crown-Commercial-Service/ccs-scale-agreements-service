package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.OrganisationEntityIdNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.MappingService;
import uk.gov.crowncommercial.dts.scale.service.agreements.service.OrganisationService;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.OrganizationDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;


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

    @GetMapping("/{id}")
    public OrganizationDetail getOrganisationDetailByEntityId(@PathVariable(value = "id") final int id) {
        log.debug("Get Organisation called with findOrganisationById: {}", id);
        return mappingService.mapOrganisationToOrganizationDetail(organisationService.findOrganisationById(id));
    }

//    @GetMapping("/name/{legalName}")
//    public OrganizationDetail getOrganisationDetailByLegalName(@PathVariable(value = "legalName") final String legalName) {
//        log.debug("Get Organisation called with legalName: {}", legalName);
//        return mappingService.mapOrganisationToOrganizationDetail(organisationService.findOrganisationByLegalName(legalName));
//    }

    @GetMapping("/dunsNumber/{entityId}")
    public OrganizationDetail getOrganisationDetailByEntityId(@PathVariable(value = "entityId") final String entityId) {
        log.debug("Get Organisation called with entityId: {}", entityId);
        return mappingService.mapOrganisationToOrganizationDetail(organisationService.findOrganisationByEntityId(entityId));
    }

    @PutMapping("/update/{id}")
    public OrganizationDetail replaceOrganisation(@RequestBody Organisation newOrganisation, @PathVariable int id) {

        Organisation result = organisationService.findOrganisationById(id)
            .map(organisation -> {
                organisation.setLegalName(newOrganisation.getLegalName());
                organisation.setIncorporationDate(newOrganisation.getIncorporationDate());
                organisation.setUri(newOrganisation.getUri());
                organisation.setIncorporationCountry(newOrganisation.getIncorporationCountry());
                return organisationService.updateOrganisation(organisation);
            });
//            .orElseGet(() -> {
//                return organisationService.save(newOrganisation);
//            });

        return mappingService.mapOrganisationToOrganizationDetail(result);

    }

    @PostMapping("/create")
    public OrganizationDetail createOrganisation(@RequestBody Organisation newOrganisation) {
        Organisation model = null;

        try{
            model = organisationService.findOrganisationByEntityId(newOrganisation.getEntityId());
        } catch( OrganisationEntityIdNotFoundException e ) {
            model = organisationService.createOrganisation(newOrganisation);
        }

        return mappingService.mapOrganisationToOrganizationDetail(model);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrganisation(@PathVariable(value = "id") final int id) {
        organisationService.deleteOrganisation(id);
    }
}