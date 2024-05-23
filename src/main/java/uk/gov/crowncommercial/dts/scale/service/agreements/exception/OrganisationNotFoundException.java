package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class OrganisationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Organisation with legal name of '%s' not found";

    public OrganisationNotFoundException(final String legalName) {
        super(String.format(ERROR_MSG_TEMPLATE, legalName));
    }
}
