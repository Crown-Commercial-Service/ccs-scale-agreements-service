package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class OrganisationLegalNameNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OrganisationLegalNameNotFoundException(final String LegalName) {
        super(String.format("Organisation with Legal Name '%s' not found", LegalName));
    }

}
