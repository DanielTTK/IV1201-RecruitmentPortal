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
public class LoginFormTest {

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
    void gettersAndValidation() {
        LoginForm f = new LoginForm();
        f.setUsername("user1");
        f.setPassword("passwordd");

        assertThat(f.getUsername()).isEqualTo("user1");
        assertThat(f.getPassword()).isEqualTo("passwordd");

        Set<ConstraintViolation<LoginForm>> violations = validator.validate(f);
        assertThat(violations).isEmpty();

        // blank username triggers constraint
        f.setUsername("");
        violations = validator.validate(f);
        assertThat(violations).isNotEmpty();

        validatorFactory.close();

        // blank password triggers constraint
        f.setPassword("");
        violations = validator.validate(f);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void userLoginSuccessAndAdminLoginFailureOnUserPage() throws Exception {
        // create an applicant
        Person p = new Person();
        p.setName("Bob");
        p.setSurname("Applicant");
        p.setUsername("user1");
        p.setEmail("user1@example.com");
        p.setPnr("12340000-0001");
        p.setPassword(passwordEncoder.encode("userpass"));
        p.setRoleId(2); // applicant
        p.setLegacy(false);
        personRepository.save(p);

        // wrong password --> redirect to login page with error
        mockMvc.perform(formLogin().user("user1").password("wrongpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loginPage?error"));

        // correct password ----> redirected to /userPage
        mockMvc.perform(formLogin().user("user1").password("userpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userPage"));

        // admin tries to login at user portal --> should get role-mismatch redirect
        Person admin = new Person();
        admin.setName("Recruiter");
        admin.setSurname("Admin");
        admin.setUsername("adminImpostor27");
        admin.setEmail("proudForumModerator123@example.com");
        admin.setPnr("12340000-0002");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setRoleId(1); // recruiter
        admin.setLegacy(false);
        personRepository.save(admin);

        mockMvc.perform(formLogin().user("adminImpostor27").password("adminpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/loginPage?error"));
    }
}
