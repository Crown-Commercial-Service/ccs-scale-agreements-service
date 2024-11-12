package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.RoleType;

public interface RoleTypeRepo extends JpaRepository<RoleType, Integer> {

    RoleType findById(int id);

}
