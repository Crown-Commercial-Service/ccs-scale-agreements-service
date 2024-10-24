package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.SupplierStatus;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Supplier Service.
 *
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierService {

    @Autowired
    private final OrganisationRepo organisationRepo;

    @Autowired
    private final LotOrganisationRoleRepo lotOrganisationRoleRepo;

    @Autowired
    private final RoleTypeRepo roleTypeRepo;

    @Autowired
    private final ContactPointLotOrgRoleRepo contactPointLotOrgRoleRepo;

    @Autowired
    private final ContactPointReasonRepo contactPointReasonRepo;

    /**
     * Find a organisation by its legal name.
     *
     * @param legalName Legal name of the organisation
     * @return Organisation
     */
    public Organisation findOrganisationByLegalName(final String legalName) {
        log.debug("findOrganisationByLegalName: {}", legalName);

        return organisationRepo.findByLegalName(legalName).orElseThrow(() -> new OrganisationNotFoundException(legalName) );
    }

    /**
     * Find a organisation by its legal name.
     *
     * @param scheme scheme of the organisation
     * @param entityId entity ID of the organisation
     * @return Organisation
     */
    public Organisation findOrganisationBySchemeAndEntityId(final String scheme, final String entityId) {
        log.debug("findOrganisationBySchemeAndEntityId: {}", scheme, entityId);

        return organisationRepo.findByRegistryCodeAndEntityId(scheme, entityId).orElseThrow(() -> new OrganisationNotFoundException(scheme, entityId) );
    }

    /**
     * Create or update the Organisation that is passed in
     *
     * @param newOrganisation Organisation
     * @return Organisation
     */
    public Organisation createOrUpdateOrganisation(final Organisation newOrganisation) {
        return organisationRepo.findByLegalName(newOrganisation.getLegalName())
                .map(organisation -> {
                    organisation.setEntityId            (newOrganisation.getEntityId());
                    organisation.setRegistryCode        (newOrganisation.getRegistryCode());
                    organisation.setLegalName           (newOrganisation.getLegalName());
                    organisation.setBusinessType        (newOrganisation.getBusinessType());
                    organisation.setUri                 (newOrganisation.getUri());
                    organisation.setStatus              (newOrganisation.getStatus());
                    organisation.setIncorporationDate   (newOrganisation.getIncorporationDate());
                    organisation.setIncorporationCountry(newOrganisation.getIncorporationCountry());
                    organisation.setCountryName         (newOrganisation.getCountryName());
                    organisation.setIsSme               (newOrganisation.getIsSme());
                    organisation.setIsVcse              (newOrganisation.getIsVcse());
                    organisation.setIsActive            (newOrganisation.getIsActive());

                    organisationRepo.saveAndFlush(organisation);
                    return findOrganisationByLegalName(organisation.getLegalName());
                }).orElseGet(() -> {
                    organisationRepo.saveAndFlush(newOrganisation);
                    return findOrganisationByLegalName(newOrganisation.getLegalName());
                });
    }




    /**
     * Assign relation between lot and organisation (supplier relationship)
     * If contact detail is valid, assign contact detial relationship (streetAddress, postalCode, countryCode, countryName is not empty)
     *
     * @param lot Lot
     * @param newOrganisation Organisation
     * @param newContactDetail ContactDetail
     * @param actionBy String
     * @param status SupplierStatus
     */
    public void addSupplierRelationship(final Lot lot, Organisation newOrganisation, ContactDetail newContactDetail, String actionBy, SupplierStatus status){
        Organisation o = this.createOrUpdateOrganisation(newOrganisation);

        //      Supplier contact reason id is 3
        ContactPointReason supplierContactReason = contactPointReasonRepo.findById(3);

        //Supplier role type id is 2
        RoleType supplierRoleType = roleTypeRepo.findById(2);

        LotOrganisationRole saveLor = lotOrganisationRoleRepo.findByLotIdAndOrganisationIdAndRoleType(lot.getId(), o.getId(), supplierRoleType)
                .map(lor -> {
                    lor.setUpdatedAt(LocalDateTime.now());
                    lor.setUpdatedBy(actionBy);
                    lor.setStatus(SupplierStatus.getChar(status));

                    lotOrganisationRoleRepo.saveAndFlush(lor);
                    return lor;
                }).orElseGet(() -> {
                    LotOrganisationRole lor = new LotOrganisationRole();
                    lor.setLotId(lot.getId());
                    lor.setOrganisation(o);
                    lor.setRoleType(supplierRoleType);
                    lor.setStartDate(LocalDate.now());
                    lor.setCreatedBy(actionBy);
                    lor.setCreatedAt(LocalDateTime.now());
                    lor.setStatus(SupplierStatus.getChar(status));

                    lotOrganisationRoleRepo.saveAndFlush(lor);
                    return lor;
                });

        if (newContactDetail != null){
            contactPointLotOrgRoleRepo.findFirstByLotOrganisationRoleIdAndContactPointReasonOrderByIdAsc(saveLor.getId(), supplierContactReason)
                    .map(cplor -> {
                        ContactDetail cd = cplor.getContactDetail();
                        cd.setEffectiveTo(newContactDetail.getEffectiveTo());
                        cd.setStreetAddress(newContactDetail.getStreetAddress());
                        cd.setLocality(newContactDetail.getLocality());
                        cd.setRegion(newContactDetail.getRegion());
                        cd.setPostalCode(newContactDetail.getPostalCode());
                        cd.setCountryCode(newContactDetail.getCountryCode());
                        cd.setCountryName(newContactDetail.getCountryName());
                        cd.setUprn(newContactDetail.getUprn());
                        cd.setEmailAddress(newContactDetail.getEmailAddress());
                        cd.setTelephoneNumber(newContactDetail.getTelephoneNumber());
                        cd.setFaxNumber(newContactDetail.getFaxNumber());
                        cd.setUrl(newContactDetail.getUrl());
                        if (newContactDetail.getName()!= null && !newContactDetail.getName().isEmpty()) { cplor.setContactPointName(newContactDetail.getName());}
                        contactPointLotOrgRoleRepo.saveAndFlush(cplor);
                        return cplor;
                    }).orElseGet(() ->{
                        ContactPointLotOrgRole cplor = new ContactPointLotOrgRole();
                        cplor.setLotOrganisationRoleId(saveLor.getId());
                        cplor.setContactPointReason(supplierContactReason);
                        cplor.setContactDetail(newContactDetail);
                        if (newContactDetail.getName()!= null && !newContactDetail.getName().isEmpty()) { cplor.setContactPointName(newContactDetail.getName());}
                        contactPointLotOrgRoleRepo.saveAndFlush(cplor);
                        return cplor;
                    });
        }
    }

    /**
     * find the existing organisation 
     * try update its name if the new name is not taken by another organisation
     * try to update its registry code and entity id if is not taken by another organisation 
     *
     * @param organisationName the existing organisation name
     * @param organisation organisation with the detail you want to update
     * @return a string of either the updated name or the existing organisation's name
     */
    public String partialSaveOrganisation(String organisationName, Organisation organisation){
        Organisation existingOrganisation = findOrganisationByLegalName(organisationName);
        
        String newName = organisation.getLegalName();
        String newRegistryCode = organisation.getRegistryCode();
        String newEntityId = organisation.getEntityId();

        try{
            if(newName != null){
                findOrganisationByLegalName(newName);
                throw new InvalidOrganisationException("legal name", newName);
            }
        }catch(OrganisationNotFoundException e ){
            existingOrganisation.setLegalName(newName);
        }

        try{
            if(newRegistryCode != null && newEntityId != null){
                findOrganisationBySchemeAndEntityId(newRegistryCode, newEntityId);
                throw new InvalidOrganisationException("scheme:id", newRegistryCode + ":"+ newEntityId);
            }
        }catch(OrganisationNotFoundException e ){
            existingOrganisation.setRegistryCode(organisation.getRegistryCode());
            existingOrganisation.setEntityId(organisation.getEntityId());
        }

        organisationRepo.saveAndFlush(existingOrganisation);
        return newName == null ? existingOrganisation.getLegalName() : newName;
    }
}
