package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactPointReason;

public interface ContactPointReasonRepo extends JpaRepository<ContactPointReason, Integer> {

    ContactPointReason findById(int Id);
}
