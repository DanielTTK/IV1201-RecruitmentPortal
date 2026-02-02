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

@Controller
public class RegisterController {
    @GetMapping("/registerForm")
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping({ "/register", "/registerForm" })
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form,
            BindingResult bindingResult) {
        if (form.getPassword() != null && form.getConfirmedPassword() != null &&
                !form.getPassword().equals(form.getConfirmedPassword())) {
            bindingResult.rejectValue("confirmedPassword", "error.mismatch", "Passwords do not match");
        } // Messages in english atm, change for future

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
