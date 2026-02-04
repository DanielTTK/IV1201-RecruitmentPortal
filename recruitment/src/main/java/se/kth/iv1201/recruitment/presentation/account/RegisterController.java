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

    @PostMapping({ "/register", "/registerForm" })
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form,
            BindingResult bindingResult) {
        if (form.getPassword() != null && form.getConfirmedPassword() != null &&
                !form.getPassword().equals(form.getConfirmedPassword())) {
            bindingResult.rejectValue("confirmedPassword", "error.mismatch", "Passwords do not match");
        } 

    if (bindingResult.hasErrors()) {
        return "register";
    }

    accountService.registerUser(form);
    return "register_success";
    }
}
