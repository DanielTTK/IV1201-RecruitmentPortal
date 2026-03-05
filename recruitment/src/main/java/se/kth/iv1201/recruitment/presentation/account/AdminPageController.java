package se.kth.iv1201.recruitment.presentation.account;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import se.kth.iv1201.recruitment.application.ApplicationService;
import se.kth.iv1201.recruitment.domain.Application;
import se.kth.iv1201.recruitment.repository.ApplicationRepository;

@Controller
public class AdminPageController {

    private final ApplicationRepository applicationRepository;

    public AdminPageController(ApplicationRepository applicationRepository,
            ApplicationService applicationService) {
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/adminPage")
    public String adminPage(Model model) {

        List<Application> applications = applicationRepository.findAll();

        model.addAttribute("applications", applications);

        return "adminPage";
    }
}
