package uk.gov.crowncommercial.dts.scale.service.agreements.controller;

import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import uk.gov.crowncommercial.dts.scale.service.agreements.model.ApiError;
import uk.gov.crowncommercial.dts.scale.service.agreements.model.ApiErrors;

/**
 * Global Error Handling.
 *
 */
@ControllerAdvice
public class AgreementsExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Generic Error Handler.
	 * 
	 * TODO: For security reasons we may want to hide internal error messages (e.g.
	 * return a code and error instance identifier to the user and write details in
	 * the logs) - to be discussed.
	 * 
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
				"An error occured processing your request", ex.getMessage());
		ApiErrors apiErrors = new ApiErrors(Arrays.asList(apiError));
		return handleExceptionInternal(ex, apiErrors, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
