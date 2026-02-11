package se.kth.iv1201.recruitment.presentation.account;

import java.util.ArrayList;
import java.util.List;

public class CompetenceProfile {

    private String workingBefore;
    private String motivation;
    private String aboutYou;

    private String startDate;
    private String endDate;

    private String prevExperience;

    private String prevExperienceYears;

    private List<DateRange> dateRanges = new ArrayList<>();

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
