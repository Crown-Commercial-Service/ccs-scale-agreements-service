package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

/**
 * Thrown when trying to call an endpoint that exists, but is not fully implemented.
 */
public class MethodNotImplementedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public MethodNotImplementedException(final String msg) {
    super(msg);
  }

}
