package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

/**
 * Lot resource not found exception
 */
public class ProcurementQuestionTemplateNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  public static final String ERROR_MSG_TEMPLATE =
      "Template ID '%s' not found";

  public ProcurementQuestionTemplateNotFoundException(final Integer templateId) {
    super(String.format(ERROR_MSG_TEMPLATE, templateId));
  }

}
