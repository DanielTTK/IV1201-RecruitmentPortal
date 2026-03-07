package se.kth.iv1201.recruitment.presentation.account;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import se.kth.iv1201.recruitment.application.ApplicationService;
import se.kth.iv1201.recruitment.domain.Application;

/**
 * Controller for handling requests to the admin page.
 * 
 * @return the admin page view 
 */
@Controller
public class AdminPageController {
    
    private final ApplicationService applicationService;
    public AdminPageController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

   @GetMapping("/adminPage")
    public String adminPage(Model model) {
    List<Application> applications = applicationService.getAllApplications();
    model.addAttribute("applications", applications);
    return "adminPage";
    }
}
