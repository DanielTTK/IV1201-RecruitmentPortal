package se.kth.iv1201.recruitment.presentation.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * This class is used to make it possible for the user to add multiple
 * expertises.
 * Inlcudes the type of expertise and years of the expertise.
 * 
 * Also includes getters and setters. 
 */
public class Experiences {
    
    @NotBlank(message = "Previous Experience must be selected.")
    private String expertise;

    @NotNull(message = "Experience Years must be selected.")
    private Integer years;

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
