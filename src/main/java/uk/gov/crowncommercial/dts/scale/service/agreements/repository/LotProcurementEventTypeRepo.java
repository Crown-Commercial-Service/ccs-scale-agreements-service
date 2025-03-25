package uk.gov.crowncommercial.dts.scale.service.agreements.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.entity.LotProcurementEventType;

/**
 * Lot Procurement Event Type Data Repository.
 */
@Repository
public interface LotProcurementEventTypeRepo extends JpaRepository<LotProcurementEventType, Integer> {}
