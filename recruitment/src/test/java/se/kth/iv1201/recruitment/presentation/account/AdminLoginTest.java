package se.kth.iv1201.recruitment.presentation.account;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.repository.ApplicationRepository;
import se.kth.iv1201.recruitment.repository.AvailabilityRepository;
import se.kth.iv1201.recruitment.repository.CompetenceProfileRepository;
import se.kth.iv1201.recruitment.repository.CompetenceRepository;
import se.kth.iv1201.recruitment.domain.Application;
import se.kth.iv1201.recruitment.domain.ApplicationStatus;
import se.kth.iv1201.recruitment.domain.Availability;
import se.kth.iv1201.recruitment.domain.CompetenceProfile;
import se.kth.iv1201.recruitment.domain.Competence;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Admin login and page access.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

        @Autowired
        private ApplicationRepository applicationRepository;

        @Autowired
        private AvailabilityRepository availabilityRepository;

        @Autowired
        private CompetenceProfileRepository competenceProfileRepository;

        @Autowired
        private CompetenceRepository competenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void adminLoginAndAccessAdminPage() throws Exception {
                // create a competence and a applicant with a submitted application in the db
                Competence comp = new Competence();
                comp.setName("Java");
                competenceRepository.save(comp);

                Person applicant = new Person();
                applicant.setName("Bob");
                applicant.setSurname("Applicant");
                applicant.setUsername("bobTheApplicant999");
                applicant.setEmail("bobTheApplicant999@example.com");
                applicant.setPnr("12340000-1234");
                applicant.setPassword(passwordEncoder.encode("userpass"));
                applicant.setRoleId(2); // applicant
                applicant.setLegacy(false);
                personRepository.save(applicant);

                // create application and related records
                Application app = new Application();
                app.setPerson(applicant);
                app.setStatus(ApplicationStatus.UNHANDLED);
                Application savedApp = applicationRepository.save(app);

                Availability av = new Availability();
                av.setPerson(applicant);
                av.setFromDate(java.time.LocalDate.now().plusDays(1));
                av.setToDate(java.time.LocalDate.now().plusDays(5));
                availabilityRepository.save(av);

                CompetenceProfile profile = new CompetenceProfile();
                profile.setPerson(applicant);
                profile.setCompetence(comp);
                profile.setYearsOfExperience(3);
                competenceProfileRepository.save(profile);

        // create an admin recruiter in the DB
        Person admin = new Person();
        admin.setName("Recruiter");
        admin.setSurname("Admin");
        admin.setUsername("recruiter1");
        admin.setEmail("recruiter@example.com");
        admin.setPnr("12340001-1234");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setRoleId(1); // 1 == recruiter
        admin.setLegacy(false);
        personRepository.save(admin);

        // perform admin form login processingurl?
        MvcResult loginResult = mockMvc.perform(
                formLogin().user("recruiter1").password("adminpass").loginProcessingUrl("/admin/login")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/adminPage"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        // access admin page
        mockMvc.perform(get("/adminPage").session(session))
                .andExpect(status().isOk());

        // Admin should be able to see applications
        var appsForPerson = applicationRepository.findAllByPersonPersonId(applicant.getPersonId());
        org.assertj.core.api.Assertions.assertThat(appsForPerson).isNotEmpty();

        // Admin changes status of the applications
        Application toUpdate = applicationRepository.findById(savedApp.getApplicationId()).orElseThrow();
        toUpdate.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.save(toUpdate);

        Application updated = applicationRepository.findById(savedApp.getApplicationId()).orElseThrow();
        org.assertj.core.api.Assertions.assertThat(updated.getStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
    }
}
