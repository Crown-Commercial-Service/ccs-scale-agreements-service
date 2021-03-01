package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

/**
 * Lot resource not found exception
 */
public class LotNotFoundException extends RuntimeException {

  static final String ERROR_MSG_TEMPLATE = "Lot number '%s' for agreement number '%s' not found";

  private static final long serialVersionUID = 1L;

  public LotNotFoundException(final String lotNumber, final String caNumber) {
    super(String.format(ERROR_MSG_TEMPLATE, lotNumber, caNumber));
  }

}
