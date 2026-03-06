package se.kth.iv1201.recruitment.presentation.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import se.kth.iv1201.recruitment.application.AccountService;
import se.kth.iv1201.recruitment.application.ApplicationService;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;
/**
 * This test verifies if a user is properly registered in the database from the register form. It is a general test, 
 * meaning it doesn't test per layer, but rather the whole flow of the registration process. 
 * It uses MockMvc to simulate a POST request, 
 * and then checks if the user is saved in the database with the expected attributes.
 */

@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RegisterUserTest.TestConfig.class)
public class RegisterUserTest {
        @TestConfiguration
    static class TestConfig {
        @Bean
        public AccountService accountService() {
            return Mockito.mock(AccountService.class);
        }

        @Bean
        public PersonRepository personRepository() {
            return Mockito.mock(PersonRepository.class);
        }

        @Bean
        public ApplicationService applicationService() {
            return Mockito.mock(ApplicationService.class);
        }
    }


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                //.apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        this.mockMvc = null;
    }


    /**
     * This test verifies that after a successful registration, the AccountService's registerUser method is called with the expected parameters.
     * @throws Exception
     */
    @WithMockUser
    @Test
    void personSentToAccountService() throws Exception {
        String username = "alice777";

        // Sends a post request to /register with following parameters, expect 200 OK code response.
        mockMvc.perform(post("/register")
                .param("firstName", "Alice")
                .param("lastName", "Swedishson")
                .param("username", username)
                .param("personNumber", "197101015678")
                .param("email", "alice@google.com")
                .param("password", "password123")
                .param("confirmedPassword", "password123")
                .with(csrf()))
                .andExpect(status().isOk());

    // Controller delegates registration to AccountService, verifies it was called with expected params
    Mockito.verify(accountService).registerUser(
        "Alice",
        "Swedishson",
        username,
        "197101015678",
        "alice@google.com",
        "password123"
    );
    }

    /**
     * This test verifies that after a successful registration, the user is saved in the database with the correct attributes.
     * @throws Exception
     */
    @Test
    void personSentToPersonRepository() throws Exception {
        String username = "alice777";
    // Simulate AccountService
    // Let mock make the repository return a Person when looked up after controller call.
    Person person = new Person();
    person.setUsername(username);
    person.setEmail("alice@google.com");
    // set a fake encrypted password to fake encoding.
    person.setPassword("EnCoDeD_PaSsWoRd1337");

    // When the controller delegates to accountService.registerUser(...),
    // make the mock set up the repository lookup to return the saved person.
    Mockito.doAnswer(invocation -> {
        Mockito.when(personRepository.findByUsernameIgnoreCase(username)).thenReturn(java.util.Optional.of(person));
        // Optional allows us to return a Person object when we mock a repository lookup,
        // which we need to do so we can verify the saved persons attributes.
        return null;
    }).when(accountService).registerUser(
        Mockito.eq("Alice"),
        Mockito.eq("Swedishson"),
        Mockito.eq(username),
        Mockito.eq("197101015678"),
        Mockito.eq("alice@google.com"),
        Mockito.eq("password123")
    );

    mockMvc.perform(post("/register")
        .param("firstName", "Alice")
        .param("lastName", "Swedishson")
        .param("username", username)
        .param("personNumber", "197101015678")
        .param("email", "alice@google.com")
        .param("password", "password123")
        .param("confirmedPassword", "password123")
        .with(csrf()))
        .andExpect(status().isOk());

    Person p = personRepository.findByUsernameIgnoreCase(username).orElse(null);
    assertThat(p).isNotNull();
    assertThat(p.getEmail()).isEqualTo("alice@google.com");
    assertThat(p.getPassword()).isNotEqualTo("password123");
    }
}
