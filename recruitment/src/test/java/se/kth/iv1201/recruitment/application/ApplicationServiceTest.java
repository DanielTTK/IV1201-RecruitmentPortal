package se.kth.iv1201.recruitment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.kth.iv1201.recruitment.application.error.ApplicationAlreadySubmitted;
import se.kth.iv1201.recruitment.domain.Application;
import se.kth.iv1201.recruitment.domain.ApplicationStatus;
import se.kth.iv1201.recruitment.domain.Availability;
import se.kth.iv1201.recruitment.domain.Competence;
import se.kth.iv1201.recruitment.domain.CompetenceProfile;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.presentation.account.CompetenceProfileForm;
import se.kth.iv1201.recruitment.presentation.account.DateRangeForm;
import se.kth.iv1201.recruitment.presentation.account.ExperiencesForm;
import se.kth.iv1201.recruitment.repository.ApplicationRepository;
import se.kth.iv1201.recruitment.repository.AvailabilityRepository;
import se.kth.iv1201.recruitment.repository.CompetenceProfileRepository;
import se.kth.iv1201.recruitment.repository.CompetenceRepository;
import se.kth.iv1201.recruitment.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CompetenceRepository competenceRepository;

    @Mock
    private CompetenceProfileRepository competenceProfileRepository;

    @Mock
    private AvailabilityRepository availabilityRepository;

    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(applicationRepository, personRepository, availabilityRepository, competenceProfileRepository, competenceRepository);
    }

    // See "AccountServiceTest" for explanations of testing techniques and objects used here, like ArgumentCaptor and Optional.
    
    @Test
    void submitApplicationSuccessSavesAll() {
        // Pprepare a person and form with one date range and one experience
        Person person = new Person();
        person.setPersonId(42);

        when(personRepository.findByUsernameIgnoreCase("bob")).thenReturn(Optional.of(person));
        when(applicationRepository.existsByPersonPersonId(42)).thenReturn(false);

        Competence competence = new Competence();
        competence.setCompetenceId(7);
        competence.setName("Carpentry");
        when(competenceRepository.findByNameIgnoreCase("Carpentry")).thenReturn(Optional.of(competence));

        // Prepare a form with one date range and one experience
        CompetenceProfileForm form = new CompetenceProfileForm();
    DateRangeForm dr = new DateRangeForm();
    dr.setStartDate(LocalDate.of(2026,1,1));
    dr.setEndDate(LocalDate.of(2026,1,31));
    form.getDateRanges().add(dr);

    ExperiencesForm exp = new ExperiencesForm();
    exp.setExpertise("Carpentry");
    exp.setYears(3);
    form.getExperiences().add(exp);

        // Make repository.save calls return the passed objector a new application 
        // allows service to continue and run logic that need verification.
        when(applicationRepository.save(any(Application.class))).thenAnswer(invoke -> {
            Application a = invoke.getArgument(0);
            a.setApplicationId(101);
            return a;
        });

        // Submit 
        applicationService.submitApplication("bob", form);

        // Verify  that application saved
        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(applicationRepository).save(captor.capture());
        Application model = captor.getValue();
        assertThat(model.getPerson()).isEqualTo(person);
        //assertThat(model.getStatus()).isNotNull();
        assertThat(model.getStatus()).isEqualTo(ApplicationStatus.UNHANDLED);

    // capture list passed to saveAll and verify contents. 
    // verify entities have correct person and competence
    @SuppressWarnings({"unchecked", "rawtypes"})
    ArgumentCaptor<List<Availability>> availCaptor = (ArgumentCaptor<List<Availability>>) (ArgumentCaptor) ArgumentCaptor.forClass(List.class);
    verify(availabilityRepository).saveAll(availCaptor.capture());
    List<Availability> availability = availCaptor.getValue();
        assertThat(availability).hasSize(1);
        assertThat(availability.get(0).getPerson()).isEqualTo(person);

        assertThat(availability.get(0).getFromDate())
            .isEqualTo(LocalDate.of(2026,1,1));
        assertThat(availability.get(0).getToDate())
            .isEqualTo(LocalDate.of(2026,1,31));

        // Verify competence profile saved
    @SuppressWarnings({"unchecked", "rawtypes"})
    ArgumentCaptor<List<CompetenceProfile>> cpCaptor = (ArgumentCaptor<List<CompetenceProfile>>) (ArgumentCaptor) ArgumentCaptor.forClass(List.class);
    verify(competenceProfileRepository).saveAll(cpCaptor.capture());
    List<CompetenceProfile> savedProfiles = cpCaptor.getValue();
        assertThat(savedProfiles).hasSize(1);
        assertThat(savedProfiles.get(0).getPerson()).isEqualTo(person);
        assertThat(savedProfiles.get(0).getCompetence().getName()).isEqualTo("Carpentry");
    }

    @Test
    void submitApplicationPersonNotFoundThrows() {
        when(personRepository.findByUsernameIgnoreCase("noone5235257774")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> applicationService.submitApplication("noone5235257774", new CompetenceProfileForm()));
    }

    @Test
    void submitApplicationAlreadySubmittedThrows() {
        Person person = new Person();
        person.setPersonId(55);
        when(personRepository.findByUsernameIgnoreCase("sam")).thenReturn(Optional.of(person));
        when(applicationRepository.existsByPersonPersonId(55)).thenReturn(true);

        assertThrows(ApplicationAlreadySubmitted.class, () -> applicationService.submitApplication("sam", new CompetenceProfileForm()));
    }

    @Test
    void submitApplicationCompetenceNotFoundThrows() {
        Person person = new Person();
        person.setPersonId(66);
        when(personRepository.findByUsernameIgnoreCase("samuel31434141")).thenReturn(Optional.of(person));
        when(applicationRepository.existsByPersonPersonId(66)).thenReturn(false);

        CompetenceProfileForm form = new CompetenceProfileForm();
        // Add a valid date range so ApplicationPersistenceValidator.validate(form) pass
    DateRangeForm dr = new DateRangeForm();
    dr.setStartDate(LocalDate.of(2026, 2, 1));
    dr.setEndDate(LocalDate.of(2026, 2, 28));
    form.getDateRanges().add(dr);
    ExperiencesForm exp = new ExperiencesForm();
    exp.setExpertise("UnknownSkill");
    exp.setYears(1);
    form.getExperiences().add(exp);

        when(competenceRepository.findByNameIgnoreCase("UnknownSkill")).thenReturn(Optional.empty());

        // Ensure save() returns a non-null Application so the service processes
        //  competences and triggers expected exception.
        when(applicationRepository.save(any(Application.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThrows(IllegalArgumentException.class, () -> applicationService.submitApplication("samuel31434141", form));
    }

    @Disabled("Withdraw application functionality was removed.")
    @Test
    void withdrawApplicationSuccessDeletesApplication() {
        // person is also owner of application
        Person person = new Person();
        person.setPersonId(200);
        when(personRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase
            ("bob@example.com", "bob@example.com"))
            .thenReturn(Optional.of(person));

        Application app = new Application();
        app.setApplicationId(300);
        Person owner = new Person();
        owner.setPersonId(200); // same as requester
        app.setPerson(owner);

        when(applicationRepository.findById(300)).thenReturn(Optional.of(app));

        //withdraw
        //applicationService.withdrawApplication("bob@example.com", 300);

        // Verify application deleted and entries remved
        verify(applicationRepository).delete(app);
        verify(availabilityRepository).deleteAllByPersonPersonId(200);
        verify(competenceProfileRepository).deleteAllByPersonPersonId(200);
    }

    @Disabled("Withdraw application functionality was removed.")
    @Test
    void withdrawApplicationAndApplicationNotFoundThrows() {
        Person person = new Person();
        person.setPersonId(201);
        when(personRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase("alice@example.com", "alice@example.com")).thenReturn(Optional.of(person));

        when(applicationRepository.findById(400)).thenReturn(Optional.empty());

        //assertThrows(IllegalArgumentException.class, () -> applicationService.withdrawApplication("alice@example.com", 400));
    }

    @Disabled("Withdraw application functionality was removed.")
    @Test
    void withdrawApplicationNotOwnerThrows() {
        // conMan is different from application owner
        Person conMan = new Person();
        conMan.setPersonId(210);
        when(personRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase("charlie@example.com", "charlie@example.com")).thenReturn(Optional.of(conMan));

        Application app = new Application();
        app.setApplicationId(310);
        Person owner = new Person();
        owner.setPersonId(999); // different id than conMan
        app.setPerson(owner);

        when(applicationRepository.findById(310)).thenReturn(Optional.of(app));

        //assertThrows(IllegalArgumentException.class, () -> applicationService.withdrawApplication("charlie@example.com", 310));
    }
}
