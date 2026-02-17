package se.kth.iv1201.recruitment.domain;


/**
 * Represents the status of an application.
 *
 * Stored as a string in the {@code application.status} column.
 *
 * Values must match what exists/allowed in the database.
 */


public enum ApplicationStatus {
    /**
     * Application exists but has not been submitted yet.
     */
    DRAFT,

    /**
     * Application has been submitted by the applicant.
     */
    SUBMITTED
}