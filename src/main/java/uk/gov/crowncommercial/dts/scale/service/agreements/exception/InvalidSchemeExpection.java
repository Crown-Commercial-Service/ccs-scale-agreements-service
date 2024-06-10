package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidSchemeExpection extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid scheme '%s' passed in";

    public InvalidSchemeExpection(final String scheme) {super(String.format(ERROR_MSG_TEMPLATE, scheme));}

    public InvalidSchemeExpection() {super("Invalid scheme passed in");}
}
