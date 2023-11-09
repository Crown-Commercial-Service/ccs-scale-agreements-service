package uk.gov.crowncommercial.dts.scale.service.agreements.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.ContactDetailRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactDetailService {

    private final ContactDetailRepo contactDetailRepo;

    public ContactDetail createContactDetail(ContactDetail cd) {
        return contactDetailRepo.saveAndFlush(cd);
    }

    public ContactDetail createOrUpdateOrganisation(final ContactDetail newCD){

        return contactDetailRepo.findFirstByEmailAddressAndStreetAddressAndEffectiveFrom(newCD.getEmailAddress(), newCD.getStreetAddress(), newCD.getEffectiveFrom())
                .map(cd -> {
                    cd.setEffectiveFrom(newCD.getEffectiveFrom());
                    cd.setEffectiveTo(newCD.getEffectiveTo());
                    cd.setStreetAddress(newCD.getStreetAddress());
                    cd.setLocality(newCD.getLocality());
                    cd.setRegion(newCD.getRegion());
                    cd.setPostalCode(newCD.getPostalCode());
                    cd.setCountryCode(newCD.getCountryCode());
                    cd.setCountryName(newCD.getCountryName());
                    cd.setUprn(newCD.getUprn());
                    cd.setEmailAddress(newCD.getEmailAddress());
                    cd.setTelephoneNumber(newCD.getTelephoneNumber());
                    cd.setFaxNumber(newCD.getFaxNumber());
                    cd.setUrl(newCD.getUrl());
                    contactDetailRepo.saveAndFlush(cd);
                    return cd;
                })
                .orElseGet(() -> {
                    contactDetailRepo.saveAndFlush(newCD);
                    return newCD;
                });

    }

}
