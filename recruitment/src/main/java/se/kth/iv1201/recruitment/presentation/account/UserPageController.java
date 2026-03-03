package se.kth.iv1201.recruitment.presentation.account;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import se.kth.iv1201.recruitment.application.ApplicationService;
import se.kth.iv1201.recruitment.domain.Application;
import se.kth.iv1201.recruitment.repository.ApplicationRepository;
import se.kth.iv1201.recruitment.repository.PersonRepository;

/**
 * Controller for the user page, where users can view their submitted applications and withdraw them if they wish to.
 * 
 * The controller retrieves the user's information and their applications from the database and populates the model for the user page view.
 * It also handles the withdrawal of applications by calling the ApplicationService to delete the application and associated data.
 */


@Controller
public class UserPageController {

    private final PersonRepository personRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;

    public UserPageController(PersonRepository personRepository, ApplicationRepository applicationRepository, ApplicationService applicationService) {
        this.personRepository = personRepository;
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
    }


    /**
     * Handles GET requests to the user page. It retrieves the user's information and their applications from the database and populates the 
     * model for the user page view.
     * 
     * @param model the model to populate with user information and applications
     * @param principal the security principal containing the user's authentication information
     * @return the user page view
     */
    @GetMapping("/userPage")
    public String userPage(Model model, Principal principal) {
        String identifier = principal.getName();

        var personOpt = personRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier);
        if (personOpt.isEmpty()) {
            model.addAttribute("name", "User");
            model.addAttribute("applications", List.of());
            return "userPage";
        }

        var person = personOpt.get();
        List<Application> applications =
                applicationRepository.findAllByPersonPersonId(person.getPersonId());

        model.addAttribute("name", person.getName());
        model.addAttribute("applications", applications);
        return "userPage";
    }

    @PostMapping("/application/withdraw")
    public String withdraw(@RequestParam("applicationId") Integer applicationId, Principal principal) {
        applicationService.withdrawApplication(principal.getName(), applicationId);
        return "redirect:/userPage";
    }
}