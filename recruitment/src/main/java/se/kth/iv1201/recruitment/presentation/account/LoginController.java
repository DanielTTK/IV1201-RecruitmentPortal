/**
 * Shows the login page and handles login submissions.
 *
 * Do server-side validation here for HGP 25, show friendly error messages, delegate the
 * real logic to AccountService. Log outcomes via the service (task 9), never log passwords.
 */

package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;

/**
 * Controller for log in and handle authentication for the user
 * 
 * It handles the displaying of the login page and the process from submitting.
 * 
 */

@Controller
public class LoginController {

    @GetMapping("/loginPage")
    public String login() {
        return "login";
    }

    /**
     * Processes the login form and validates if it is correct or not
     * 
     * @param form           is the login form containing both username and password
     * @param bindningResult contains the validation results for the form inputs
     * @return if success it returns user to competence profile, if error, same page
     *         is returned
     */

    @PostMapping("/loginPage")
    public String loggedIn(@Valid @ModelAttribute LoginForm form,
            BindingResult bindningResult) {

        //! REMOVE LOGS LATER, only for debugging 
        // System.out.println("Username: " + form.getUsername());
        // System.out.println("Password: " + form.getPassword());

        if (bindningResult.hasErrors()) {
            return "loginPage";
        }

        //TODO - add a check that controls that username and password are correct and then forward user to /competenceProfile
        return "redirect:/competenceProfile";
    }

}
