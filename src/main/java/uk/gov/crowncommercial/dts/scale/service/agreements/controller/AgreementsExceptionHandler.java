package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.ApiError;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.ApiErrors;

/**
 * Global Error Handling.
 *
 */
@ControllerAdvice
@RestController
public class AgreementsExceptionHandler extends ResponseEntityExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ApiErrors handleUnknownException(final Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
				"An error occured processing your request", ex.getMessage());
		return new ApiErrors(Arrays.asList(apiError));
	}
}