package se.kth.iv1201.recruitment.application.error;

/**
 * Exception thrown when a username is already in use.
 */
public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException(String username) {
        super("Username already taken: " + username);
    }
}