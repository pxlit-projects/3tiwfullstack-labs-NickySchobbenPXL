package be.pxl.services.exception;

public class OrganizationNotFoundException extends RuntimeException{

    public OrganizationNotFoundException(String message) {
        super(message);
    }
}
