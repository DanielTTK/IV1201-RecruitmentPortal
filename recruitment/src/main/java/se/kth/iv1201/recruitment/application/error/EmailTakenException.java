package se.kth.iv1201.recruitment.application.error;

/**
 * Exception thrown when an email is already in use.
 */
public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String email) {
        super("Email already in use: " + email);
    }
}
