/**
 * Fields from the registration form + validation rules (HGP 25).
 *
 * Keep constraints here (not blank, email format, password length). Any DB checks
 * (like “username already taken”) belong in the service though.
 */

package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class RegisterForm {

    @GetMapping("/registerForm")
    public String registerForm(Model model) {
        return "register";
    }

}
