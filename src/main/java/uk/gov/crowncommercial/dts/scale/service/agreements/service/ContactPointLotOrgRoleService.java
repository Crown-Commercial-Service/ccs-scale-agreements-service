package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.ContactPointLotOrgRoleRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactPointLotOrgRoleService {

    private final ContactPointLotOrgRoleRepo contactPointLotOrgRoleRepo;

    public ContactPointLotOrgRole createContactPointLotOrgRole(ContactPointLotOrgRole cplor){
        return contactPointLotOrgRoleRepo.saveAndFlush(cplor);
    }

    public ContactPointLotOrgRole createOrUpdateContactPointLotOrgRole (final ContactPointLotOrgRole newCplor){

        return contactPointLotOrgRoleRepo.findByContactDetailIdAndContactPointReasonIdAndLotOrganisationRoleId(newCplor.getContactDetail().getId(), newCplor.getContactPointReason().getId(), newCplor.getLotOrganisationRole().getId())
                .map(cplor -> {
                    cplor.setContactPointName(newCplor.getContactPointName());
                    cplor.setContactDetail(newCplor.getContactDetail());
                    cplor.setContactPointReason(newCplor.getContactPointReason());
                    cplor.setLotOrganisationRole(newCplor.getLotOrganisationRole());
                    cplor.setEffectiveFrom(newCplor.getEffectiveFrom());
                    cplor.setEffectiveTo(newCplor.getEffectiveTo());
                    cplor.setPrimary(newCplor.getPrimary());
                    contactPointLotOrgRoleRepo.saveAndFlush(cplor);
                    return cplor;

        })
        .orElseGet(() -> {
            contactPointLotOrgRoleRepo.saveAndFlush(newCplor);
            return newCplor;
        });
    }
}
