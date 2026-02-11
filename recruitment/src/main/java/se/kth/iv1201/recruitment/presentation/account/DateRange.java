package se.kth.iv1201.recruitment.presentation.account;

import java.time.LocalDate;
import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * This class includes a start date and an end date for applicants.
 * The class is used in CompetenceProfile to make it possible to add multiple
 * periods that the applicant is available to work.
 */

public class DateRange {
    @FutureOrPresent(message = "Start date cannot be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date cannot be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * Getter for the start date
     * 
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Setter for start date
     * 
     * @param startDate the start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for end date.
     * 
     * @return end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Setter for end date
     * 
     * @param endDate the end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DateRange{" + "startDate='" + startDate + '\'' + ", endDate='" + endDate + '\'' + '}';
    }
}
