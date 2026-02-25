package se.kth.iv1201.recruitment.application.error;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String email) {
        super("Email already in use: " + email);
    }
}
