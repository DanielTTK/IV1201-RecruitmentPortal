package se.kth.iv1201.recruitment.presentation.account;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
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
    private RegisterForm form;

    @BeforeEach
    void setUp() {
        form = new RegisterForm();
        setDefaults(form);
    }

    @AfterEach
    void tearDown() {
        form = null;
    }

    void setDefaults(RegisterForm form) {
        form.setFirstName("John");
        form.setLastName("Doe");
        form.setUsername("johndoe");
    }

    @Test
    void checkForNotBlank() {
        form.setPersonNumber("");
        form.setEmail("");
        form.setPassword("");
        form.setConfirmedPassword("");

        Set<ConstraintViolation<RegisterForm>> violations = validator.validate(form);
        // Expecting at least 7 violations for @NotBlank
        assertThat(violations).hasSizeGreaterThanOrEqualTo(7);
    }


    /**
     * Test that a valid RegisterForm passes all validation constraints without violations.
     * Jakarta Bean Validation stores each "failed" constraint as a ConstraintViolation object inside "violations". 
     * If the form is valid, this "set" should be empty.
     */
    @Test
    void checkForFlatConstraints() {
        form.setPersonNumber("123456789101");
        form.setEmail("john.doe@example.com");
        form.setPassword("password");
        form.setConfirmedPassword("password");

        Set<ConstraintViolation<RegisterForm>> violations = validator.validate(form);
        assertThat(violations).withFailMessage("Expected no validation violations for valid RegisterForm").isEmpty();
        assertThat(violations).noneMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void checkForInvalidEmail() {
        form.setPersonNumber("123456789101");
        form.setEmail("invalid-email");
        form.setPassword("password");
        form.setConfirmedPassword("password");

        Set<ConstraintViolation<RegisterForm>> violations = validator.validate(form);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
        assertThat(violations).withFailMessage("Expected invalid email to cause validation error, but it did not.").anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }


    @Disabled("This is tested in RegisterControllerTest, as the constraint is written in 'RegisterController' and not in 'RegisterForm'. If we move the constraint to 'RegisterForm', this test should be enabled.")
    @Test
    void checkForPasswordMismatch() {
        form.setPersonNumber("123456789101");
        form.setEmail("john.doe@example.com");

        form.setPassword("password");
        form.setConfirmedPassword("differentPassword");

        Set<ConstraintViolation<RegisterForm>> violations = validator.validate(form);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
    }
}
