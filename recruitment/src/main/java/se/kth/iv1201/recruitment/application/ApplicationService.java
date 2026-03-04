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
import se.kth.iv1201.recruitment.util.ApplicationPersistenceValidator;
import se.kth.iv1201.recruitment.domain.CompetenceProfile;
import se.kth.iv1201.recruitment.presentation.account.CompetenceProfileForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    /**
     * Constructs the ApplicationService with required dependencies.
     * The repositories are injected to allow the service to perform necessary database operations for handling applications.
     * 
     * @param applicationRepository
     * @param personRepository
     * @param availabilityRepository
     * @param competenceProfileRepository
     * @param competenceRepository
     */
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

        log.info("APPLICATION_SUBMIT_ATTEMPT personId={} availabilityCount={} competenceCount={}",
                person.getPersonId(),
                form != null && form.getDateRanges() != null ? form.getDateRanges().size() : 0,
                form != null && form.getExperiences() != null ? form.getExperiences().size() : 0);


        if (applicationRepository.existsByPersonPersonId(person.getPersonId())) {
            log.info("APPLICATION_SUBMIT_BLOCKED_ALREADY_EXISTS personId={}", person.getPersonId());
            throw new ApplicationAlreadySubmitted();
        }

        ApplicationPersistenceValidator.validate(form); //validation in integration layer!

        Application application = new Application();
        application.setPerson(person);
        application.setStatus(ApplicationStatus.UNHANDLED);

        Application savedApp = applicationRepository.save(application);

        log.info("APPLICATION_SUBMIT_SUCCESS applicationId={} personId={}",
                savedApp.getApplicationId(), person.getPersonId());


        //Provided List (iterable) of dates that is submitted and collect them into a list for "batch" persistence. 
        List<Availability> availabilities = form.getDateRanges().stream()
                .map(dateRange -> {
                    Availability availability = new Availability();
                    availability.setPerson(person);
                    availability.setFromDate(dateRange.getStartDate());
                    availability.setToDate(dateRange.getEndDate());
                    return availability;
                })
                .toList();
        //Persist all Availability entities in a single repository call.    
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

    /**
     * Withdraws an existing application for a user. 
     * This method deletes the Application entity and all associated Availability and CompetenceProfile entities for the user.
     * 
     * @param identifier
     * @param applicationId
     */
    @Transactional
    public void withdrawApplication(String identifier, Integer applicationId) {
        Person person = personRepository
                .findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));

        log.info("APPLICATION_WITHDRAW_ATTEMPT personId={} applicationId={}",
                person.getPersonId(), applicationId);

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        if (!app.getPerson().getPersonId().equals(person.getPersonId())) {
            log.warn("APPLICATION_WITHDRAW_FORBIDDEN_NOT_OWNER requesterPersonId={} applicationOwnerPersonId={} applicationId={}",
                    person.getPersonId(), app.getPerson().getPersonId(), applicationId);
            throw new IllegalArgumentException("Not your application");
        }

        applicationRepository.delete(app);

        //Clean up associated availability and competence profile entries
        availabilityRepository.deleteAllByPersonPersonId(person.getPersonId());
        competenceProfileRepository.deleteAllByPersonPersonId(person.getPersonId());

        log.info("APPLICATION_WITHDRAW_SUCCESS personId={} applicationId={}",
                person.getPersonId(), applicationId);
    }
}