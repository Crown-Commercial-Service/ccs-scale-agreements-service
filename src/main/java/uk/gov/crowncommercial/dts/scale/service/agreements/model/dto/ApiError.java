package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import lombok.Data;

/**
 * API Error Wrapper.
 */
@Data
public class ApiError {

  private final String status;
  private final String title;
  private final String detail;
}
