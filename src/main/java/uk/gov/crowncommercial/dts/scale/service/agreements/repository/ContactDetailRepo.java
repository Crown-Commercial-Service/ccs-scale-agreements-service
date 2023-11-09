package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;

import java.time.LocalDate;
import java.util.Optional;

public interface ContactDetailRepo extends JpaRepository<ContactDetail, Integer> {

    ContactDetail findById(int id);

    Optional<ContactDetail> findFirstByEmailAddressAndStreetAddressAndEffectiveFrom(String emailAddress, String streetAddress, LocalDate effectiveFrom);
}
