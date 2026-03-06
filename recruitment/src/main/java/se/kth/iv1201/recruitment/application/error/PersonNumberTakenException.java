package se.kth.iv1201.recruitment.application.error;

/**
 * Exception thrown when a person number is already in use.
 */
public class PersonNumberTakenException extends RuntimeException {
    public PersonNumberTakenException() {
        super("Person number already in use");
    }
}
