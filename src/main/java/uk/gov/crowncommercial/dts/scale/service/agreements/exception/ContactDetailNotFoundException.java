package uk.gov.crowncommercial.dts.scale.service.agreements.exception;

public class ContactDetailNotFoundException extends RuntimeException {
    public ContactDetailNotFoundException(Object key) {
        super("Contact detail not found for key: " + key);
    }
}
