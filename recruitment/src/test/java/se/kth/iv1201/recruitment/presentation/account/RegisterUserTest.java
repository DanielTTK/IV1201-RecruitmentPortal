package se.kth.iv1201.recruitment.presentation.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Optional;

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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import se.kth.iv1201.recruitment.application.AccountService;
import se.kth.iv1201.recruitment.application.ApplicationService;
import se.kth.iv1201.recruitment.application.ResendEmailService;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;
/**
 * This test verifies if a user is properly registered in the database from the register form. It is a general test, 
 * meaning it doesn't test per layer, but rather the whole flow of the registration process. 
 * It uses MockMvc to simulate a POST request, 
 * and then checks if the user is saved in the database with the expected attributes.
 * 
 * TODO: Need to complement this test with per layer tests (controller, service, repository) to 
 * ensure all layers are properly tested and to make debugging easier when a test fails.
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

        @Bean
        public ResendEmailService resendEmailService() {
            return Mockito.mock(ResendEmailService.class);
        }
    }


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }

    @WithMockUser
    @Test
    void personSavedInDBAfterPosting() throws Exception {
        String username = "alice777";

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

        Optional<Person> opt = personRepository.findByUsernameIgnoreCase(username);
        assertThat(opt).isPresent();
        Person p = opt.get();
        assertEquals("Alice", p.getName(), "First name should be saved correctly");
        assertEquals("Swedishson", p.getSurname(), "Last name should be saved correctly");
        assertEquals("alice@google.com", p.getEmail(), "Email should be saved correctly");
        assertNotNull(p.getPassword(), "Password should not be null");
        assertNotEquals("password123", p.getPassword(), "Password needs to be hashed");
        assertEquals(Integer.valueOf(2), p.getRoleId());
    }
}
