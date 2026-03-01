package se.kth.iv1201.recruitment.presentation.account;

import se.kth.iv1201.recruitment.application.AccountService;
import se.kth.iv1201.recruitment.application.ApplicationService;
import se.kth.iv1201.recruitment.application.ResendEmailService;
import se.kth.iv1201.recruitment.repository.PersonRepository;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test for {@link RegisterController} covering all constraints and cases.
 */



@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RegisterControllerTest.TestConfig.class)
public class RegisterControllerTest {
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

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(springSecurity()) // add the Spring Security filter chain to MockMvc
                .build();
    }

    @AfterEach
    void tearDown() {
        this.mockMvc = null;
    }

    @WithMockUser
    @Test
    void checkPasswordMismatch() throws Exception {

        // Perform an HTTP POST to the /register endpoint with different passwords.
        // We include a CSRF token using .with(csrf()).
        mockMvc.perform(post("/register")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("username", "johndoe")
                .param("personNumber", "199001011234")
                .param("email", "johndoe@example.com")
                .param("password", "samesame")
                .param("confirmedPassword", "different")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("registerForm", "confirmedPassword"));
    }

    @WithMockUser
    @Test
    void checkPasswordMatch() throws Exception {

        // Perform an HTTP POST to the /register endpoint with matching passwords.
        // We include a CSRF token using .with(csrf()).
        mockMvc.perform(post("/register")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("username", "johndoe")
                .param("personNumber", "199001011234")
                .param("email", "johndoe@example.com")
                .param("password", "samesame")
                .param("confirmedPassword", "samesame")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasNoErrors("registerForm"));
    }
}
