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


/**
 * Service responsible for handling the submission of job applications.
 *
 * This service coordinates the creation of Application, Availability,
 * and CompetenceProfile entities.
 *
 * Business rules enforced:
 * - A user can only submit one application.
 * - All referenced competences must exist.
 *
 * All operations are executed within a transactional context.
 * If any step fails, the entire submission is rolled back.
 */

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

    /**
     * The method is transactional: If any persistence operation fails,
     * the entire transaction will be rolled back.
     * It submits a job application for a given user.
     *
     * Steps performed:
     * - Retrieves the Person associated with the username.
     * - Checks that no previous application exists.
     * - Creates and saves a new Application entity.
     * - Saves all availability date ranges.
     * - Saves all competence profiles.
     *
     * @param username the username of the applicant (case-insensitive)
     * @param form contains availability periods and competence experience data
     *
     * @throws IllegalArgumentException if the user does not exist or a competence cannot be found
     * @throws ApplicationAlreadySubmitted if the user has already submitted an application
     */
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

        // Provided List (iterable) of dates that is submitted and collect them into a list for "batch" persistence. 
        List<Availability> availabilities = form.getDateRanges().stream()
                .map(dateRange -> {
                    Availability availability = new Availability();
                    availability.setPerson(person);
                    availability.setFromDate(dateRange.getStartDate());
                    availability.setToDate(dateRange.getEndDate());
                    return availability;
                })
                .toList();
        // Persist all Availability entities in a single repository call.    
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