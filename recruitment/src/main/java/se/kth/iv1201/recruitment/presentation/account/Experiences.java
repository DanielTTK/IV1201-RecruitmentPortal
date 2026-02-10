package se.kth.iv1201.recruitment.presentation.account;

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
        return "Experiences{" +
                "expertise='" + expertise + '\'' +
                ", years='" + years + '\'' +
                '}';
    }
}
