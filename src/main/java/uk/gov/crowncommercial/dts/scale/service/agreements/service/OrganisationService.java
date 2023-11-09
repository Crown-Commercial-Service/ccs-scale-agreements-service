package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.OrganisationEntityIdNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.OrganisationIdNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.OrganisationRepo;


/**
 * Organisation Service.
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganisationService {

    private final OrganisationRepo organisationRepo;

//    public List<Organisation> getOrganisations() {
//        log.debug("getOrganisations");
//        return organisationRepo.findAll();
//    }

    public Organisation findOrganisationById(final int id) {
        log.debug("findOrganisationById: {}", id);

        final Organisation organisationModel = organisationRepo.findById(id);

        if (organisationModel == null) {
            throw new OrganisationIdNotFoundException(id);
        }

        return organisationModel;
    }

    public Organisation findOrganisationByEntityId(final String entityId) {
        log.debug("findOrganisationByEntityId: {}", entityId);

        final Organisation organisationModel = organisationRepo.findByEntityId(entityId);

        if (organisationModel == null) {
            throw new OrganisationEntityIdNotFoundException(entityId);
        }

        return organisationModel;
    }

//    public Organisation findOrganisationByLegalName(final String legalName) {
//        log.debug("findOrganisationByLegalName: {}", legalName);
//        return organisationRepo.findByLegalName(legalName);
//    }

    public Organisation createOrganisation(Organisation organisation) {
        log.debug("createOrganisation: {}", organisation);
        return organisationRepo.saveAndFlush(organisation);
    }

    public Organisation updateOrganisation(final Organisation newOrganisation) {
        log.info("updating organisation: {}", newOrganisation);
        organisationRepo.saveAndFlush(newOrganisation);
        return newOrganisation;
    }

    public Organisation createOrUpdateOrganisation(final Organisation newOrganisation) {

        return organisationRepo.findByLegalName(newOrganisation.getLegalName())
                .map(organisation -> {
                    organisation.setEntityId(newOrganisation.getEntityId());
                    organisation.setRegistryCode(newOrganisation.getRegistryCode());
                    organisation.setLegalName(newOrganisation.getLegalName());
                    organisation.setBusinessType(newOrganisation.getBusinessType());
                    organisation.setUri(newOrganisation.getUri());
                    organisation.setStatus(newOrganisation.getStatus());
                    organisation.setIncorporationDate(newOrganisation.getIncorporationDate());
                    organisation.setIncorporationCountry(newOrganisation.getIncorporationCountry());
                    organisation.setCountryName(newOrganisation.getCountryName());
                    organisation.setIsSme(newOrganisation.getIsSme());
                    organisation.setIsVcse(newOrganisation.getIsVcse());
                    organisation.setIsActive(newOrganisation.getIsActive());
                    organisationRepo.saveAndFlush(organisation);
                    return organisation;
            })
        .orElseGet(() -> {
            organisationRepo.saveAndFlush(newOrganisation);
            return newOrganisation;
        });
    }

    public void deleteOrganisation(final int id) {
        log.info("deleting organisation with id: {}", id);
        organisationRepo.delete(findOrganisationById(id));
    }

}