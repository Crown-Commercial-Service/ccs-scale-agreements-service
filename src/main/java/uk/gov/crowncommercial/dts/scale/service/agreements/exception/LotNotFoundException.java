package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

/**
 * Lot resource not found exception
 */
public class LotNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  public static final String ERROR_MSG_TEMPLATE =
      "Lot number '%s' for agreement number '%s' not found";

  public LotNotFoundException(final String lotNumber, final String caNumber) {
    super(String.format(ERROR_MSG_TEMPLATE, lotNumber, caNumber));
  }

}
