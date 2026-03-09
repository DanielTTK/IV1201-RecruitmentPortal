package se.kth.iv1201.recruitment.application.error;

/**
 * Exception thrown when an application is already submitted.
 * Used in ApplicationService to indicate that a person has already submitted an application and cannot submit another one.
 */ 

public class ApplicationAlreadySubmitted extends RuntimeException {
    public ApplicationAlreadySubmitted() { //error message to double check that the application is already sent
        super("Application already submitted.");
    }
}
