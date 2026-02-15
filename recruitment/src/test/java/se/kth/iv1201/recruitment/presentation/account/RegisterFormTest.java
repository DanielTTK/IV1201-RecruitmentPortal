package se.kth.iv1201.recruitment.presentation.account;

import java.util.Set;

//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 */
@SpringBootTest
class RegisterFormTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /* 
    @BeforeEach
    void setUp() {

        
    }

    @AfterEach
    void tearDown() {
    }
    */

    @Test
    void checkForNotBlank() {
        RegisterForm form = new RegisterForm();
        form.setFirstName("");
        form.setLastName("");
        form.setUsername("");
        form.setPersonNumber("");
        form.setEmail("");
        form.setPassword("");
        form.setConfirmedPassword("");

        Set<ConstraintViolation<RegisterForm>> violations = validator.validate(form);
        // Expecting at least 7 violations for @NotBlank
        assertThat(violations).hasSizeGreaterThanOrEqualTo(7);
    }

    void setDefaults(RegisterForm form) {
        form.setFirstName("John");
        form.setLastName("Doe");
        form.setUsername("johndoe");
    }


    /**
     * Test that a valid RegisterForm passes all validation constraints without violations.
     * Jakarta Bean Validation stores each "failed" constraint as a ConstraintViolation object inside "violations". 
     * If the form is valid, this "set" should be empty.
     */
    @Test
    void checkForConstraints() {
        RegisterForm form = new RegisterForm();
        setDefaults(form);
        form.setPersonNumber("123456789101");
        form.setEmail("john.doe@example.com");
        form.setPassword("password");
        form.setConfirmedPassword("password");

        Set<ConstraintViolation<RegisterForm>> violations = validator.validate(form);
        assertThat(violations).withFailMessage("Expected no validation violations for valid RegisterForm").isEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }


    @Disabled("This is tested in RegisterControllerTest, as the constraint is written in 'RegisterController' and not in 'RegisterForm'. If we move the constraint to 'RegisterForm', this test should be enabled.")
    @Test
    void checkForPasswordMismatch() {
        RegisterForm form = new RegisterForm();
        setDefaults(form);
        form.setPersonNumber("123456789101");
        form.setEmail("john.doe@example.com");

        form.setPassword("password");
        form.setConfirmedPassword("differentPassword");

        Set<ConstraintViolation<RegisterForm>> violations = validator.validate(form);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
    }
}
