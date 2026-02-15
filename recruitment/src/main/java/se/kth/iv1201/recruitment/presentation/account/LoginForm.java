/**
 * Fields from the login form + validation annotations (HGP 25).
 *
 */

package se.kth.iv1201.recruitment.presentation.account;

import jakarta.validation.constraints.NotBlank;

/**
 * Class for login, contains password and username, and getters and setters
 */

public class LoginForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
