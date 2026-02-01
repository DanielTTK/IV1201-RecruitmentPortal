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

@Controller
public class RegisterController {
    @GetMapping("/registerForm")
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerForm") RegisterForm form,
            BindingResult bindingResult, Model model) {
        if (form.getFirstName() == null || form.getFirstName().isBlank()) {
            bindingResult.rejectValue("firstName", "error.blank", "First name is required");
        }
        if (form.getLastName() == null || form.getLastName().isBlank()) {
            bindingResult.rejectValue("lastName", "error.blank", "Last name is required");
        }
        if (form.getPersonNumber() == null || form.getPersonNumber().length() != 12) {
            bindingResult.rejectValue("personNumber", "error.length", "Person number must be in format YYYYMMDDXXXX");
        }
        if (form.getEmail() == null || form.getEmail().isBlank()) {
            bindingResult.rejectValue("email", "error.blank", "Email is required");
        } else if (!form.getEmail().contains("@")) {
            bindingResult.rejectValue("email", "error.invalid", "Email must contain @");
        }
        if (form.getConfirmedEmail() == null || form.getConfirmedEmail().isBlank()) {
            bindingResult.rejectValue("confirmedEmail", "error.blank", "Confirmed email is required");
        } else if (!form.getConfirmedEmail().contains("@")) {
            bindingResult.rejectValue("confirmedEmail", "error.invalid", "Email must contain @");
        }
        if (form.getPassword() == null || form.getPassword().length() < 6) {
            bindingResult.rejectValue("password", "error.length", "Password must be at least 6 characters");
        }
        if (form.getConfirmedPassword() == null || form.getConfirmedPassword().length() < 6) {
            bindingResult.rejectValue("confirmedPassword", "error.length",
                    "Confirmed password must be at least 6 characters");
        }

        if (form.getPassword() != null && form.getConfirmedPassword() != null &&
                !form.getPassword().equals(form.getConfirmedPassword())) {
            bindingResult.rejectValue("confirmedPassword", "error.mismatch", "Passwords do not match");
        }

        if (form.getEmail() != null && form.getConfirmedEmail() != null &&
                !form.getEmail().equals(form.getConfirmedEmail())) {
            bindingResult.rejectValue("confirmedEmail", "error.mismatch", "Emails do not match");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        // TODO: Call AccountService to save user
        return "register_success";
    }
}
