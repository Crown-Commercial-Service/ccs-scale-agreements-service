package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class BenefitNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "'%s' benefit not found";

    public BenefitNotFoundException(final String caNumber) {
        super(String.format(ERROR_MSG_TEMPLATE, caNumber));
    }
}
