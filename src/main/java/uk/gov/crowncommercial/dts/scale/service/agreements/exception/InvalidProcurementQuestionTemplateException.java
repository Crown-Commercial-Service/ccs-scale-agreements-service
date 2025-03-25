package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidProcurementQuestionTemplateException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MSG_TEMPLATE = "Invalid template data format, invalid '%s' for id %s";

    public InvalidProcurementQuestionTemplateException(final String missingField, final Integer id) {
        super(String.format(ERROR_MSG_TEMPLATE, missingField,id));
    }
}
