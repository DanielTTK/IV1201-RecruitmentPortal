package se.kth.iv1201.recruitment.presentation.account;

/**
 * This class includes a start date and an end date for applicants. 
 * The class is used in CompetenceProfile to make it possible to add multiple periods
 * that the applicant is available to work.
 */

public class DateRange {
    private String startDate;
    private String endDate;

    /**
     * Getter for the start date
     * 
     * @return the start date in the format YYYY-MM-DD
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Setter for start date
     * 
     * @param startDate which is the start date in format YYYY-MM-DD
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for end date.
     * 
     * @return end date in formate YYYY-MM-DD
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Setter for end date
     * 
     * @param endDate which is the end date in formate YYYY-MM-DD
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
