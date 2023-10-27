package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.Organisation;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.OrganisationRepo;

import javax.transaction.Transactional;


/**
 * Organisation Service.
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganisationService {

    private final OrganisationRepo organisationRepo;


    /**
     * Get all organisations.
     *
     * @return list of organisations
     */
    public List<Organisation> getOrganisations() {
        log.debug("getOrganisations");
        return organisationRepo.findAll();
    }

    /**
     * Find a specific organisation by registryCode.
     *
     * @param entityId organisation entityId
     * @return Organisation
     */
    public Organisation findOrganisationByRegistryCode(final String entityId) {
        log.debug("findOrganisationByName: {}", entityId);

        final Organisation organisationModel = organisationRepo.findByEntityId(entityId);

        return organisationModel;
    }

    @Transactional
    public Organisation saveAndFlush(final Organisation organisation) {
        log.info("saving organisation: {}", organisation);
        organisationRepo.saveAndFlush(organisation);
        return organisation;
    }

}
