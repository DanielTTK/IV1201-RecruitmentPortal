package se.kth.iv1201.recruitment.presentation.account;

/**
 * This class is used to make it possible for the user to add multiple
 * expertises.
 * Inlcudes the type of expertise and years of the expertise.
 * 
 * Also includes getters and setters. 
 */
public class Experiences {
    private String expertise;
    private Integer years; // changed the data type from String -> Integer

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    @Override
    public String toString() {
        return "Experiences{" + "expertise='" + expertise + '\'' + ", years='" + years + '\'' + '}';
    }
}
