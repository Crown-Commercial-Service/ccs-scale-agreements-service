package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.AgreementNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.LotNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ApiError;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ApiErrors;

/**
 * Centralised error handling for application and container derived error conditions.
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalErrorHandler implements ErrorController {

  public static final String ERR_MSG_DEFAULT = "An error occurred processing the request";
  public static final String ERR_MSG_VALIDATION = "Validation error processing the request";
  public static final String ERR_MSG_NOT_FOUND = "Resource not found";

  public static final String ERR_MSG_VALIDATION_DESCRIPTION = "Invalid request";
  public static final String ERR_MSG_NOT_FOUND_DESCRIPTION =
      "The resource you were looking for could not be found";
  public static final String ERR_MSG_UNKNOWN_DESCRIPTION = "An unknown error has occurred";

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ValidationException.class, HttpMessageNotReadableException.class,
      MethodArgumentTypeMismatchException.class})
  public ApiErrors handleValidationException(final Exception exception) {

    log.trace("Request validation exception", exception);
    String detail = exception.getMessage();
    if (exception instanceof MethodArgumentTypeMismatchException) {
      detail =
          ((MethodArgumentTypeMismatchException) exception).getMostSpecificCause().getMessage();
    }

    final ApiError apiError =
        new ApiError(HttpStatus.BAD_REQUEST.toString(), ERR_MSG_VALIDATION, detail);
    return new ApiErrors(Arrays.asList(apiError), ERR_MSG_VALIDATION_DESCRIPTION);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({AgreementNotFoundException.class, LotNotFoundException.class})
  public ApiErrors handleResourceNotFoundException(final Exception exception) {

    log.trace("Resource not found exception", exception);

    final ApiError apiError =
        new ApiError(HttpStatus.NOT_FOUND.toString(), ERR_MSG_NOT_FOUND, exception.getMessage());
    return new ApiErrors(Arrays.asList(apiError), ERR_MSG_NOT_FOUND_DESCRIPTION);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ApiErrors handleUnknownException(final Exception exception) {

    log.error("Unknown application exception", exception);
    return new ApiErrors(null, ERR_MSG_UNKNOWN_DESCRIPTION);
  }

  @GetMapping(value = "/error")
  public ResponseEntity<ApiErrors> handleError(final HttpServletRequest request,
      final HttpServletResponse response) {

    final Object exception = request.getAttribute("javax.servlet.error.exception");

    log.error("Unknown container/filter exception", exception);

    return ResponseEntity.badRequest()
        .body(new ApiErrors(
            Arrays.asList(
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ERR_MSG_DEFAULT, "")),
            "DESC"));
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

}
