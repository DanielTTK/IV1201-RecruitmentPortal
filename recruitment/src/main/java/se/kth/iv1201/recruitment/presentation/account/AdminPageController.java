package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling requests to the admin page.
 * 
 * @return the admin page view 
 */
@Controller
public class AdminPageController {

    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }
}
