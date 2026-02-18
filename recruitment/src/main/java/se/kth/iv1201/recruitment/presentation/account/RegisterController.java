/**
 * Shows the register page and handles account creation.
 *
 * Server side validation (HGP 25), delegate creation to AccountService (like hashing, checks),
 * then show success/error. Keep UI text ready for translations (HGP 13).
 */

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

/**
 * Controller for registering an account. Connected to application to make it
 * possible to forward the post and send the data to the database.
 */
@Controller
public class RegisterController {

    private final AccountService accountService;

    public RegisterController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    /**
     * Post for the form of creating an account.
     * Checks if password is the same as confirmed password to make it possible to
     * submit.
     * 
     * @return register_success if no errors appear
     * @return same page with errors if errors appear.
     */
    @PostMapping({ "/register" })
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form,
            BindingResult bindingResult) {
        if (form.getPassword() != null && form.getConfirmedPassword() != null &&
                !form.getPassword().equals(form.getConfirmedPassword())) {
            bindingResult.rejectValue("confirmedPassword", "error.mismatch", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            accountService.registerUser(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getUsername(),
                    form.getPersonNumber(),
                    form.getEmail(),
                    form.getPassword());
            return "register_success";
        } catch (UsernameTakenException e) {
            bindingResult.rejectValue("username", "error.usernameTaken", "Username is already taken!");
            return "register";
        } catch (EmailTakenException e) {
            bindingResult.rejectValue("email", "error.email", e.getMessage());
            return "register";
        } /*catch (PersonNumberTakenException e) {
            bindingResult.rejectValue("pnr", "error.pnr", e.getMessage());
            return "register";
        } */
    }
}
