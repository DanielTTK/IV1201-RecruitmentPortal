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
    private String years;

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    @Override
    public String toString() {
        return "Experiences{" + "expertise='" + expertise + '\'' + ", years='" + years + '\'' + '}';
    }
}
