package se.kth.iv1201.recruitment.presentation.account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 * Controller for log in and handle authentication for the user.
 * Handles the displaying of the login page and the process from submitting.
 * 
 * @return the login page view
 */
@Controller
public class LoginController {

    @GetMapping("/loginPage")
    public String login(Model model) {
        model.addAttribute("loginPage", new LoginForm());
        return "login";
    }
}
