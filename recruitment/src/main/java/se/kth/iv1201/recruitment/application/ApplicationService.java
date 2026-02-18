package se.kth.iv1201.recruitment.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import se.kth.iv1201.recruitment.application.error.ApplicationAlreadySubmitted;
import se.kth.iv1201.recruitment.domain.Application;
import se.kth.iv1201.recruitment.domain.ApplicationStatus;
import se.kth.iv1201.recruitment.domain.Availability;
import se.kth.iv1201.recruitment.domain.Competence;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.ApplicationRepository;
import se.kth.iv1201.recruitment.repository.AvailabilityRepository;
import se.kth.iv1201.recruitment.repository.CompetenceProfileRepository;
import se.kth.iv1201.recruitment.repository.CompetenceRepository;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.domain.CompetenceProfile;
import se.kth.iv1201.recruitment.presentation.account.CompetenceProfileForm;




@Service
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final PersonRepository personRepository;
    private final CompetenceProfileRepository competenceProfileRepository;
    private final CompetenceRepository competenceRepository;
    private final AvailabilityRepository availabilityRepository;


    public ApplicationService(ApplicationRepository applicationRepository, 
        PersonRepository personRepository,             
        AvailabilityRepository availabilityRepository,
        CompetenceProfileRepository competenceProfileRepository,
        CompetenceRepository competenceRepository) {

            this.applicationRepository = applicationRepository;
            this.personRepository = personRepository;
            this.competenceProfileRepository = competenceProfileRepository;
            this.competenceRepository = competenceRepository;
            this.availabilityRepository = availabilityRepository;
    }

    @Transactional
    public void submitApplication(String username, CompetenceProfileForm form) {

        Person person = personRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));

        if (applicationRepository.existsByPersonPersonId(person.getPersonId())) {
            throw new ApplicationAlreadySubmitted();
        }

        Application application = new Application();
        application.setPerson(person);
        application.setStatus(ApplicationStatus.DRAFT);
        applicationRepository.save(application);

        // Iterable list of dates that is submitted, whereby we need to save all the dates selected 
        List<Availability> availabilities = form.getDateRanges().stream()
                .map(dateRange -> {
                    Availability availability = new Availability();
                    availability.setPerson(person);
                    availability.setFromDate(dateRange.getStartDate());
                    availability.setToDate(dateRange.getEndDate());
                    return availability;
                })
                .toList();
        // saveAll(): we are collecting the multiple options into a list, and are inside a transactional method        
        availabilityRepository.saveAll(availabilities);

        List<CompetenceProfile> profiles = form.getExperiences().stream()
                .map(exp -> {
                    Competence competence = competenceRepository
                            .findByNameIgnoreCase(exp.getExpertise())
                            .orElseThrow(() -> new IllegalArgumentException("Competence not found"));

                    CompetenceProfile profile = new CompetenceProfile();
                    profile.setPerson(person);
                    profile.setCompetence(competence);
                    profile.setYearsOfExperience(exp.getYears());
                    return profile;
                })
                .toList();

        competenceProfileRepository.saveAll(profiles);
    }
}