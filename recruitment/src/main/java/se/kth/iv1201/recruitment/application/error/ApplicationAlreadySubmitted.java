package se.kth.iv1201.recruitment.application.error;

public class ApplicationAlreadySubmitted extends RuntimeException {
    public ApplicationAlreadySubmitted() { //error message to double check that the application is already sent
        super("Application already submitted.");
    }
}
