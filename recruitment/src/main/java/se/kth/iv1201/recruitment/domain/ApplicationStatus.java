package se.kth.iv1201.recruitment.domain;


/**
 * Enum representing the status of a job application. It can be in one of the following states:
 * UNHANDLED: The application has been submitted but has not been reviewed by the recruiters yet.
 * ACCEPTED: The application has been reviewed and accepted by the recruiters.
 * REJECTED: The application has been reviewed and rejected by the recruiters.
 * 
 * This enum is used in the Application entity to track the current status of each application, and it can be used in the business logic to determine what actions are allowed based on the application's status.
 */


public enum ApplicationStatus {
    UNHANDLED,
    ACCEPTED,
    REJECTED
}