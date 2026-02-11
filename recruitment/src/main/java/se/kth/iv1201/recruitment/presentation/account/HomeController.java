/**
 * Start page routes ("/") and simple navigation.
 *
 */

package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}


