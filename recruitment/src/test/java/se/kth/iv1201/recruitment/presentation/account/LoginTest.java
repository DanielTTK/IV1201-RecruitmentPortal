package se.kth.iv1201.recruitment.presentation.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import se.kth.iv1201.recruitment.domain.Competence;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.repository.CompetenceRepository;
import se.kth.iv1201.recruitment.repository.ApplicationRepository;
import se.kth.iv1201.recruitment.repository.AvailabilityRepository;
import se.kth.iv1201.recruitment.repository.CompetenceProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * This test does the following:
 *  - Register through the /register endpoint
 *  - Log in and submit an application
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CompetenceRepository competenceRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private CompetenceProfileRepository competenceProfileRepository;

    @Test
    void registerAndLoginTest() throws Exception {
        // create competence
        Competence comp = new Competence();
        comp.setName("Java");
        competenceRepository.save(comp);

        // create a user in the db
        Person p = new Person();
        p.setName("Bob");
        p.setSurname("Applicant");
        p.setUsername("bob1234");
        p.setEmail("bob@example.com");
        p.setPnr("19900000-1234");
        p.setPassword(passwordEncoder.encode("secretpass"));
        p.setRoleId(2); // applicant
        p.setLegacy(false);
        personRepository.save(p);

        // login using form login. Fills security context for the session
        MvcResult loginResult = mockMvc.perform(formLogin().user("bob1234").password("secretpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userPage"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        // visit competence profile page to start the session attribute
        mockMvc.perform(get("/competenceProfile").session(session))
                .andExpect(status().isOk());

        // Post the competence form. This is stored in session before submission.
        mockMvc.perform(post("/competence")
                .session(session)
                .param("experiences[0].expertise", "Java")
                .param("experiences[0].years", "5")
                .param("dateRanges[0].startDate", LocalDate.now().plusDays(1).toString())
                .param("dateRanges[0].endDate", LocalDate.now().plusDays(10).toString())
                .param("review", "true")
                .with(csrf()))
                .andExpect(status().isOk());

        // submit the application, submit the session attr to DB 
        mockMvc.perform(post("/competenceProfile/submit").session(session).with(csrf()))
                .andExpect(status().isOk());

        // Verify application
        Person saved = personRepository.findByUsernameIgnoreCase("bob1234").orElseThrow();
        assertThat(applicationRepository.existsByPersonPersonId(saved.getPersonId())).isTrue();
        assertThat(availabilityRepository.existsByPersonPersonId(saved.getPersonId())).isTrue();
        assertThat(competenceProfileRepository.findAllByPersonPersonId(saved.getPersonId())).isNotEmpty();
    }
}
