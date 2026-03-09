package se.kth.iv1201.recruitment.util;

import java.time.LocalDate;
import se.kth.iv1201.recruitment.presentation.account.CompetenceProfileForm;

/**
 * Utility class for validating the data of a job application before it is persisted to the database.
 * This class is used by the ApplicationService to ensure that the application data meets the necessary criteria before it is saved.
 */
public final class ApplicationPersistenceValidator {

    private ApplicationPersistenceValidator() {   // Private constructor to prevent instantiation
    }

    /**
     * Validates the given CompetenceProfileForm to ensure that it contains all required information and that the data is in the correct format.
     * 
     * @param form the CompetenceProfileForm to validate
     * @throws IllegalArgumentException if the form is null, if required fields are missing, or if any of the data is in an invalid format 
     * (date ranges where start date is after end date, experience with missing expertise or years out of range)
     */
    public static void validate(CompetenceProfileForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Invalid application - missing form");
        }

        if (form.getDateRanges() == null || form.getDateRanges().isEmpty()) {
            throw new IllegalArgumentException("Invalid application - missing availability");
        }

        if (form.getExperiences() == null || form.getExperiences().isEmpty()) {
            throw new IllegalArgumentException("Invalid application - missing experience");
        }

        // Validate date ranges
        for (int i = 0; i < form.getDateRanges().size(); i++) {
            var dateRange = form.getDateRanges().get(i);

            LocalDate startDate = dateRange.getStartDate();
            LocalDate endDate = dateRange.getEndDate();

            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Invalid availability - start/end required");
            }

            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Invalid availability - start must be before or equal to end");
            }
        }

        // Validate experiences
        for (int i = 0; i < form.getExperiences().size(); i++) {
            var experience = form.getExperiences().get(i);

            String expertise = experience.getExpertise();
            Integer years = experience.getYears();

            if (expertise == null || expertise.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid experience - expertise required");
            }

            if (years == null || years < 0 || years > 5) {
                throw new IllegalArgumentException("Invalid experience - years out of range");
            }
        }
    }
}