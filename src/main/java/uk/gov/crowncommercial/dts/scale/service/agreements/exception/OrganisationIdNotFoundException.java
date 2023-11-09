package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class OrganisationIdNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OrganisationIdNotFoundException(final int id) {
        super(String.format("Organisation with Id '%s' not found", id));
    }

}
