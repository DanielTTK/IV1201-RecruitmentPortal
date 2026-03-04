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
 * 
 * @return the user page view, where the user can see their name and a list of their applications, with options to withdraw them.
 */
@Controller
public class UserPageController {

    private final PersonRepository personRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;


    /**
     * Constructs the UserPageController with the required dependencies. The PersonRepository is used to retrieve user information from the 
     * database, the ApplicationRepository is used to retrieve the user's applications, and the ApplicationService is used to handle the 
     * business logic of withdrawing an application.
     * 
     * @param personRepository
     * @param applicationRepository
     * @param applicationService
     */
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
        List<Application> applications = applicationRepository.findAllByPersonPersonId(person.getPersonId());

        model.addAttribute("name", person.getName());
        model.addAttribute("applications", applications);
        return "userPage";
    }

    /**
     * Handles POST requests to withdraw an application. It calls the ApplicationService to delete the application and associated data, 
     * and then redirects back to the user page.
     * 
     * @param applicationId
     * @param principal
     * @return
     */
    @PostMapping("/application/withdraw")
    public String withdraw(@RequestParam("applicationId") Integer applicationId, Principal principal) {
        applicationService.withdrawApplication(principal.getName(), applicationId);
        return "redirect:/userPage";
    }
}