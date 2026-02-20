package se.kth.iv1201.recruitment.presentation.account;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;

/**
 * Class for the competence profile page. Has the inputs from the form.
 */

// Changed the class name from CompetenceProfile -> CompetenceProfileForm, 
// to help the service layer differentiate the repo and presentation class
public class CompetenceProfileForm {

    private String workingBefore;
    private String motivation;
    private String aboutYou;
    private String prevExperience;

    private String prevExperienceYears;

    @Valid
    private List<DateRange> dateRanges = new ArrayList<>();

    @Valid
    private List<Experiences> experiences = new ArrayList<>();

    public String getWorkingBefore() {
        return workingBefore;
    }
    public void setWorkingBefore(String workingBefore) {
        this.workingBefore = workingBefore;
    }

    public String getPrevExperience() {
        return prevExperience;
    }

    public void setPrevExperience(String prevExperience) {
        this.prevExperience = prevExperience;
    }
    public String getPrevExperienceYears() {
        return prevExperienceYears;
    }

    public void setPrevExperienceYears(String prevExperienceYears) {
        this.prevExperienceYears = prevExperienceYears;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getAboutYou() {
        return aboutYou;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public List<DateRange> getDateRanges() {
        return dateRanges;
    }

    public void setDateRanges(List<DateRange> dateRanges) {
        this.dateRanges = dateRanges;
    }
    public List<Experiences> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experiences> experiences) {
        this.experiences = experiences;
    }

}
