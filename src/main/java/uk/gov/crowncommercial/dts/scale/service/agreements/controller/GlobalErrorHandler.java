package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Arrays;

import jakarta.xml.bind.ValidationException;

import com.rollbar.notifier.Rollbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.*;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ApiError;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.dto.ApiErrors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Centralised error handling for application and container derived error conditions.
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalErrorHandler implements ErrorController {

  @Autowired
  private Rollbar rollbar;

  public static final String ERR_MSG_VALIDATION_TITLE = "Validation error processing the request";
  public static final String ERR_MSG_VALIDATION_DESCRIPTION = "Invalid request";
  public static final String ERR_MSG_NOT_FOUND_TITLE = "Resource not found";
  public static final String ERR_MSG_NOT_FOUND_DESCRIPTION = "The resource you were looking for could not be found";
  public static final String ERR_MSG_DEFAULT_DESCRIPTION = "An unknown error has occurred";

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ValidationException.class, HttpMessageNotReadableException.class,
      MethodArgumentTypeMismatchException.class, InvalidAgreementException.class, InvalidLotException.class, InvalidSchemeExpection.class, InvalidOrganisationException.class, InvalidContactDetailExpection.class, InvalidRegulationException.class,  InvalidAgreementTypeException.class})
  public ApiErrors handleValidationException(final Exception exception) {

    rollbar.warning(exception, "Request validation exception");
    log.trace("Request validation exception", exception);
    String detail = exception.getMessage();
    if (exception instanceof MethodArgumentTypeMismatchException) {
      detail =
          ((MethodArgumentTypeMismatchException) exception).getMostSpecificCause().getMessage();
    }

    final ApiError apiError =
        new ApiError(HttpStatus.BAD_REQUEST.toString(), ERR_MSG_VALIDATION_TITLE, detail);
    return new ApiErrors(Arrays.asList(apiError), ERR_MSG_VALIDATION_DESCRIPTION);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({AgreementNotFoundException.class, LotNotFoundException.class, OrganisationNotFoundException.class})
  public ApiErrors handleResourceNotFoundException(final Exception exception) {

    rollbar.warning(exception, "Resource not found exception");
    log.trace("Resource not found exception", exception);

    final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.toString(), ERR_MSG_NOT_FOUND_TITLE, exception.getMessage());
    return new ApiErrors(Arrays.asList(apiError), ERR_MSG_NOT_FOUND_DESCRIPTION);
  }

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(MethodNotImplementedException.class)
  public ApiErrors handleMethodNotImplementedException(final Exception exception) {

    rollbar.error(exception, "Method not implemented exception");
    log.trace("Method not implemented", exception);

    ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED.toString(), ERR_MSG_NOT_FOUND_TITLE, exception.getMessage());
    return new ApiErrors(Arrays.asList(apiError), ERR_MSG_NOT_FOUND_DESCRIPTION);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ApiErrors handleUnknownException(final Exception exception) {

    rollbar.error(exception, "Unknown application exception");
    log.error("Unknown application exception", exception);
    return new ApiErrors(null, ERR_MSG_DEFAULT_DESCRIPTION);
  }

  @GetMapping(value = "/error")
  public ResponseEntity<ApiErrors> handleError(final HttpServletRequest request,
      final HttpServletResponse response) {

    final Object exception = request.getAttribute("jakarta.servlet.error.exception");

    rollbar.error("Unknown container or filter exception");
    log.error("Unknown container/filter exception", exception);

    return ResponseEntity.badRequest().body(new ApiErrors(null, ERR_MSG_DEFAULT_DESCRIPTION));
  }
}