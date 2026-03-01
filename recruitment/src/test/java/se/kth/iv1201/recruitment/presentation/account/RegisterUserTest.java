package se.kth.iv1201.recruitment.presentation.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import se.kth.iv1201.recruitment.application.AccountService;
import se.kth.iv1201.recruitment.application.ApplicationService;
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
    }


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        this.mockMvc = null;
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
}
