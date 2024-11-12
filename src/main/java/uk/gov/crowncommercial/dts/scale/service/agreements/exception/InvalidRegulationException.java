package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidRegulationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid regulation '%s' passed in. Regulation must be one of the following: [PA2023, PCR2015, PCR2006]";

    public InvalidRegulationException(final String regulation) {
        super(String.format(ERROR_MSG_TEMPLATE, regulation));
    }
}