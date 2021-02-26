package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.MethodNotImplementedException;
import uk.gov.crowncommercial.dts.scale.service.agreements.exception.ResourceNotFoundException;
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

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ValidationException.class, HttpMessageNotReadableException.class})
  public ApiErrors handleValidationException(final Exception exception) {

    log.trace("Request validation exception", exception);

    ApiError apiError =
        new ApiError(HttpStatus.BAD_REQUEST.toString(), ERR_MSG_VALIDATION, exception.getMessage());
    return new ApiErrors(Arrays.asList(apiError));
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ApiErrors handleResourceNotFoundException(final Exception exception) {

    log.trace("Resource not found exception", exception);

    ApiError apiError =
        new ApiError(HttpStatus.NOT_FOUND.toString(), ERR_MSG_NOT_FOUND, exception.getMessage());
    return new ApiErrors(Arrays.asList(apiError));
  }

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(MethodNotImplementedException.class)
  public ApiErrors handleMethodNotImplementedException(final Exception exception) {

    log.trace("Method not implemented", exception);

    ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED.toString(), ERR_MSG_NOT_FOUND,
        exception.getMessage());
    return new ApiErrors(Arrays.asList(apiError));
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ApiErrors handleUnknownException(final Exception exception) {

    log.error("Unknown application exception", exception);

    ApiError apiError =
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ERR_MSG_DEFAULT, "");
    return new ApiErrors(Arrays.asList(apiError));
  }

  @GetMapping(value = "/error")
  public ResponseEntity<ApiErrors> handleError(final HttpServletRequest request,
      final HttpServletResponse response) {

    Object exception = request.getAttribute("javax.servlet.error.exception");

    log.error("Unknown container/filter exception", exception);

    return ResponseEntity.badRequest().body(new ApiErrors(Arrays
        .asList(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ERR_MSG_DEFAULT, ""))));
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

}
