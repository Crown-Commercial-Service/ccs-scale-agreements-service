package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidLotException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid lot format, missing '%s'";

    public InvalidLotException(final String missingField) {
        super(String.format(ERROR_MSG_TEMPLATE, missingField));
    }
}
