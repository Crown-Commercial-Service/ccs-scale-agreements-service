package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String operation) {
        super("Invalid operation: " + operation);
    }
}