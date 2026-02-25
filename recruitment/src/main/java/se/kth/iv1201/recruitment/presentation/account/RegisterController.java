package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import se.kth.iv1201.recruitment.application.AccountService;
import se.kth.iv1201.recruitment.application.error.EmailTakenException;
import se.kth.iv1201.recruitment.application.error.PersonNumberTakenException;
import se.kth.iv1201.recruitment.application.error.UsernameTakenException;

import jakarta.servlet.http.HttpSession;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;

/**
 * Controller for registering an account. Connected to application to make it
 * possible to forward the post and send the data to the database.
 * 
 * @param accountService the service to handle account creation logic
 * @param personRepository the repository to look up legacy user information
 * @see AccountService for the business logic of account creation, including validation and error handling.
 */
@Controller
public class RegisterController {

    private final AccountService accountService;
    private final PersonRepository personRepository;

    public RegisterController(AccountService accountService, PersonRepository personRepository) {
        this.accountService = accountService;
        this.personRepository = personRepository;
}



/**
 * Get for the form of creating an account. If the user has been identified as a legacy user, 
 * the form will be pre-populated with the information we have about the user to make it easier for them to create an account. 
 * 
 * @param model the model to populate the form
 * @param session the session to check for legacy user information
 * @return the register page
 **/
    @GetMapping("/register")
    public String registerForm(Model model, HttpSession session) {
        Integer legacyId = (Integer) session.getAttribute("LEGACY_PERSON_ID");

        if (legacyId != null) {
            Person person = personRepository.findById(legacyId).orElse(null);
            RegisterForm form = new RegisterForm();

            if (person != null) {
                form.setFirstName(person.getName());
                form.setLastName(person.getSurname());
                form.setUsername(person.getUsername());
                form.setEmail(person.getEmail());
                form.setPersonNumber(person.getPnr());
            }

            model.addAttribute("registerForm", form);
            return "register";
        }

        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    /**
     * Post for the form of creating an account. 
     * Checks if password is the same as confirmed password to make it possible to submit.
     * 
     * @param form the form to get the data from
     * @param bindingResult the binding result to check for errors and add field errors
     * @return register_success if no errors appear
     * @return same page with errors if errors appear.
     */
    @PostMapping({ "/register" })
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult, HttpSession session) 
    {
        if (form.getPassword() != null && form.getConfirmedPassword() != null &&
                !form.getPassword().equals(form.getConfirmedPassword())) {
            bindingResult.rejectValue("confirmedPassword", "error.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {

        Integer legacyId = (Integer) session.getAttribute("LEGACY_PERSON_ID"); //Check if this is a legacy user completing their registration

        if (legacyId != null) { 
            accountService.completeLegacyUser(
                    legacyId,
                    form.getFirstName(),
                    form.getLastName(),
                    form.getUsername(),
                    form.getPersonNumber(),
                    form.getEmail(),
                    form.getPassword()
            );

            session.removeAttribute("LEGACY_PERSON_ID");
            session.removeAttribute("LEGACY_OTP");

            return "redirect:/loginPage"; //After completing legacy registration, redirect to login page
        }

            accountService.registerUser(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getUsername(),
                    form.getPersonNumber(),
                    form.getEmail(),
                    form.getPassword());
            return "register_success";
        } catch (UsernameTakenException e) {
            bindingResult.rejectValue("username", "error.usernameTaken", e.getMessage());
            return "register";
        } catch (EmailTakenException e) {
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            return "register";
        } catch (PersonNumberTakenException e) {
            bindingResult.rejectValue("personNumber", "error.pnrTaken", e.getMessage());
            return "register";
        } 
    }
}
