package uk.gov.crowncommercial.dts.scale.service.agreements.model.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * Compound Key.
 */
@Data
@Embeddable
public class LotProcurementEventTypeKey implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "lot_id")
  Integer lotId;

  @Column(name = "procurement_event_type_id")
  Integer procurementEventTypeId;
}
