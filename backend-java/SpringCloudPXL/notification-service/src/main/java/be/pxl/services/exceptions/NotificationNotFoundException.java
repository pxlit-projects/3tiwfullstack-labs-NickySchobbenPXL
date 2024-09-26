package be.pxl.services.exceptions;

public class NotificationNotFoundException extends RuntimeException{

    public NotificationNotFoundException(String message) {
        super(message);
    }
}
