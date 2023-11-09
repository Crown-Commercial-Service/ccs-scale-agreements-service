package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class OrganisationEntityIdNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OrganisationEntityIdNotFoundException(final String entityId) {
        super(String.format("Organisation with Duns Number '%s' not found", entityId));
    }

}
