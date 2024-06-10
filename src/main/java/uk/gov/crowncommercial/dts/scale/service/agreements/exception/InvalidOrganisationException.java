package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidOrganisationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid organisation format, missing '%s'";

    public InvalidOrganisationException(final String missingField) {
        super(String.format(ERROR_MSG_TEMPLATE, missingField));
    }
}
