/**
 * Shows the login page and handles login submissions.
 *
 * Do server-side validation here for HGP 25, show friendly error messages, delegate the
 * real logic to AccountService. Log outcomes via the service (task 9), never log passwords.
 */

package se.kth.iv1201.recruitment.presentation.account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 * Controller for log in and handle authentication for the user
 * 
 * It handles the displaying of the login page and the process from submitting.
 * 
 */

@Controller
public class LoginController {

    @GetMapping("/loginPage")
    public String login(Model model) {
        model.addAttribute("loginPage", new LoginForm());
        return "login";
    }
}
