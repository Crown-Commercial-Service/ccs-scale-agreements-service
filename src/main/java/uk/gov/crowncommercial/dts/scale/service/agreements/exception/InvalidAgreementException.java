package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidAgreementException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid agreement format, missing '%s'";

    public InvalidAgreementException(final String missingField) {
        super(String.format(ERROR_MSG_TEMPLATE, missingField));
    }
}
