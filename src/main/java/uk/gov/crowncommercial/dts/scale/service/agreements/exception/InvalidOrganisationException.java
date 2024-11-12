package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidOrganisationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid organisation format, missing '%s'";
    public static final String ERROR_MSG_TEMPLATE_EXISTED = "Organisation with '%s': '%s', already exist";
    public static final String ERROR_MSG_TEMPLATE_EMPTY = "Organisation body must have either legalName or a valid scheme with id in the body";

    public InvalidOrganisationException() {
        super(String.format(ERROR_MSG_TEMPLATE_EMPTY));
    }

    public InvalidOrganisationException(final String missingField) {
        super(String.format(ERROR_MSG_TEMPLATE, missingField));
    }

    public InvalidOrganisationException(final String field, String value) {
        super(String.format(ERROR_MSG_TEMPLATE_EXISTED, field, value));
    }
}
