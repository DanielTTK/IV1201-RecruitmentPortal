package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// made a temporary admin page for use case 5.5 completion
public class ApplicationsController {
    @GetMapping("/admin/applications")
    public String listApplications(Model model) {
    return "applications_admin";
    }
}
