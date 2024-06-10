package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidContactDetailExpection  extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid contact detail format, missing '%s'";

    public InvalidContactDetailExpection(final String missingField) {
        super(String.format(ERROR_MSG_TEMPLATE, missingField));
    }
}
