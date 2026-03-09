package se.kth.iv1201.recruitment.presentation.account;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;

/**
 * Form object for the competence profile page. It contains fields for the
 * user's previous work experience, and motivation.
 * It also contains lists of date ranges and experiences for the user's previous
 * work experience.
 * 
 * The form is used to capture the user's input when they fill out their
 * competence profile, and it is validated using Bean Validation annotations.
 * The controller will handle the submission of this form and save the data to
 * the database.
 */
public class CompetenceProfileForm {

    private String workingBefore;
    private String motivation;
    private String aboutYou;
    private String prevExperience;
    private String prevExperienceYears;

    @Valid
    private List<DateRangeForm> dateRanges = new ArrayList<>();

    @Valid
    private List<ExperiencesForm> experiences = new ArrayList<>();

    /**
     * Getters and setters for the form fields. These are used by Spring to bind the
     * form data to the form object when the user submits the form,
     * and to populate the form fields when the form is displayed.
     * 
     * @return the value of the form fields
     */
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

    public List<DateRangeForm> getDateRanges() {
        return dateRanges;
    }

    public void setDateRanges(List<DateRangeForm> dateRanges) {
        this.dateRanges = dateRanges;
    }

    public List<ExperiencesForm> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperiencesForm> experiences) {
        this.experiences = experiences;
    }

}
