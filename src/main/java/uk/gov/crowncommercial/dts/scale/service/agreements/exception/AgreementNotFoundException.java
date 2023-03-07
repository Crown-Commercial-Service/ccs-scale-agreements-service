package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

/**
 * Agreement resource not found exception
 */
public class AgreementNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  public static final String ERROR_MSG_TEMPLATE = "Agreement number '%s' not found";

  public AgreementNotFoundException(final String caNumber) {
    super(String.format(ERROR_MSG_TEMPLATE, caNumber));
  }

}
