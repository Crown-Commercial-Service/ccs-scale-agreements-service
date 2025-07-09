package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.ContactDetail;

public interface ContactDetailRepo extends JpaRepository<ContactDetail, Integer> {
    // Add custom queries if needed
} 