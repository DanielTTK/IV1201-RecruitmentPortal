package se.kth.iv1201.recruitment.application.error;

public class PersonNumberTakenException extends RuntimeException {
    public PersonNumberTakenException() {
        super("Person number already in use");
    }
}
