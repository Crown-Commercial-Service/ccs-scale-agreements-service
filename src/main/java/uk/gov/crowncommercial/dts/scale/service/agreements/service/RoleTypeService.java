package uk.gov.crowncommercial.dts.scale.service.agreements.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.RoleType;
import uk.gov.crowncommercial.dts.scale.service.agreements.repository.RoleTypeRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleTypeService {
    private final RoleTypeRepo roleTypeRepo;

    public RoleType getRoleTypeById(int id){
        return roleTypeRepo.findById(id);
    }

    public RoleType getRoleTypeByName(String name){
        return roleTypeRepo.findByName(name);
    }
}
