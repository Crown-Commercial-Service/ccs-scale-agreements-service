package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class ResourceNotFoundException extends RuntimeException {

  /**
   * Thrown when trying to access a resource that does not exist.
   */
  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException(final String msg) {
    super(msg);
  }

}
