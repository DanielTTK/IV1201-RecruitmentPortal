/**
 * Fields from the registration form + validation rules (HGP 25).
 *
 * Keep constraints here (not blank, email format, password length). Any DB checks
 * (like “username already taken”) belong in the service though.
 */

package se.kth.iv1201.recruitment.presentation.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank
    @Size(min = 12, max = 12, message = "Person number must consist of 12 digits")
    private String personNumber;

    @NotBlank
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String confirmedPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

}
