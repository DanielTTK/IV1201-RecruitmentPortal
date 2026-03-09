package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Controller for handling requests to the home page.
 * 
 * @return the home page view 
 */
@Controller

public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}


