package se.kth.iv1201.recruitment.presentation.account;

import se.kth.iv1201.recruitment.application.AccountService;
import se.kth.iv1201.recruitment.application.ApplicationService;
import se.kth.iv1201.recruitment.application.ResendEmailService;
import se.kth.iv1201.recruitment.repository.PersonRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Test for `RegisterController` covering all constraints and cases.
 *
 * Explanations (kept here so develoepers have context and so we can refer back to them when writing future tests):
 *
 * - SpringBootTest: boots the full Spring application for tests. This allows
 *   wiring controllers, services, repositories and exercising the
 *   application the way it runs in main, but for testing.
 *
 * - WebApplicationContext: the web-specific Spring ApplicationContext that holds
 *   web configuration (controllers, HandlerMappings, view resolvers etc). We
 *   build MockMvc from this context so the test uses the same web stack that
 *   Spring would use at runtime.
 *
 * - MockMvc: a testing utility that lets us perform HTTP like requests on
 *   Spring MVC stack without starting a HTTP server. It sends requests
 *   through DispatcherServlet, runs relevant methods and draws views.
 *
 * - springSecurity(): part of `spring-security-test`. It's a MockMvc configurer that
 *   applies the Spring Security filter chain to MockMvc so security-related behavior
 *   (authentication, CSRF protection, authorization) is consistent to main during tests.
 *
 * - csrf(): a request processor provided by `spring-security-test` that adds
 *   valid CSRF tokens to the requests. When CSRF protection is enabled in security,
 *   POST/PUT/DELETE requests require a token; `csrf()` simulates that
 *   token so the request arent rejected.
 *
 * - model(): a MockMvcResultMatchers helper that inspects the MVC Model produced by
 *   controller methods (attributes added with @ModelAttribute).
 *   `model().attributeHasFieldErrors("registerForm", "confirmedPassword")` checks
 *   that the model contains binding/validation errors on the `confirmedPassword` field
 *   of the `registerForm` object — that's how controller-level `BindingResult` errors
 *   are exposed to templates/views.
 */



@WebMvcTest(RegisterController.class)
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
                .param("password", "pw1")
                .param("confirmedPassword", "pw2")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("registerForm", "confirmedPassword"));
    }
}
