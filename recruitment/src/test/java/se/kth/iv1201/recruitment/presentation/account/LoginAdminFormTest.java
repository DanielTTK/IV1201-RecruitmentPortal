package se.kth.iv1201.recruitment.presentation.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginAdminFormTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    static void setupValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void close() {
        validatorFactory.close();
    }

    @Test
    void gettersAndSetters() {
        LoginAdminForm f = new LoginAdminForm();
        f.setUsernameAdmin("adminuser");
        f.setPasswordAdmin("adminpass");

        assertThat(f.getUsernameAdmin()).isEqualTo("adminuser");
        assertThat(f.getPasswordAdmin()).isEqualTo("adminpass");

        Set<ConstraintViolation<LoginAdminForm>> violations = validator.validate(f);

         // blank username triggers constraint
        f.setUsernameAdmin("");
        violations = validator.validate(f);
        assertThat(violations).isNotEmpty();

        validatorFactory.close();

        // blank password triggers constraint
        f.setPasswordAdmin("");
        violations = validator.validate(f);
        assertThat(violations).isNotEmpty(); 
    }

    @Test
    void gettersAndValidation() {
        LoginAdminForm f = new LoginAdminForm();
        f.setUsernameAdmin("admin");
        f.setPasswordAdmin("passport");

        assertThat(f.getUsernameAdmin()).isEqualTo("admin");
        assertThat(f.getPasswordAdmin()).isEqualTo("passport");

        Set<ConstraintViolation<LoginAdminForm>> violations = validator.validate(f);
        assertThat(violations).isEmpty();
    }

    @Test
    void adminLoginSuccessAndUserLoginFailureOnAdminPage() throws Exception {
        // create a recruiter
        Person r = new Person();
        r.setName("Recruiter");
        r.setSurname("One");
        r.setUsername("rec1");
        r.setEmail("rec1@example.com");
        r.setPnr("12345678-0001");
        r.setPassword(passwordEncoder.encode("goood!"));
        r.setRoleId(1); // recruiter
        r.setLegacy(false);
        personRepository.save(r);

    // wrong password --> redirect to admin login error page 
    mockMvc.perform(formLogin().user("rec1").password("bad").loginProcessingUrl("/admin/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loginAdmin?error"));

        // correct password --> redirected to /adminPage chaching$$$ chaching$$
        mockMvc.perform(formLogin().user("rec1").password("goood!").loginProcessingUrl("/admin/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/adminPage"));

        // user tries to login at admin portal
        Person user = new Person();
        user.setName("Applicant");
        user.setSurname("NUMBERTWOOOOO");
        user.setUsername("app2");
        user.setEmail("app2@example.com");
        user.setPnr("12345678-0002");
        user.setPassword(passwordEncoder.encode("AAAAAAAA"));
        user.setRoleId(2); // applicant
        user.setLegacy(false);
        personRepository.save(user);

        mockMvc.perform(formLogin().user("app2").password("AAAAAAAA").loginProcessingUrl("/admin/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loginAdmin?error"));
    }
}
