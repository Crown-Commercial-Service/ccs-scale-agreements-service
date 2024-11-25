package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.Regulation;

public class InvalidAgreementTypeException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid agreementType '%s' passed in. agreementType must be one of the following: [Dynamic Purchasing System, Dynamic Market, Open Framework, Closed Framework, PCR15 Framework, PCR06 Framework]";
    public static final String ERROR_MSG_TEMPLATE_FOR_REGULATION = "agreementType must be one of the following: '%s' for '%s'";

    public InvalidAgreementTypeException(final String agreementType) {
        super(String.format(ERROR_MSG_TEMPLATE, agreementType));
    }

    public InvalidAgreementTypeException(final String allowList, final Regulation regulation) {
        super(String.format(ERROR_MSG_TEMPLATE_FOR_REGULATION, allowList, regulation.getName()));
    }
}



