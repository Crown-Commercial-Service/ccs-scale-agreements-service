package uk.gov.crowncommercial.dts.scale.service.agreements.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointLotOrgRole;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointReason;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.ContactPointReasonRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactPointReasonService {
    private final ContactPointReasonRepo contactPointReasonRepo;

    public ContactPointReason getContactPointReasonByName(String name){
        return contactPointReasonRepo.findByName(name);
    }

    public ContactPointReason getContactPointReasonById(int id){
        return contactPointReasonRepo.findById(id);
    }
}
