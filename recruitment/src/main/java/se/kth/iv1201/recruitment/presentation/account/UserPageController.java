package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.ApplicationRepository;
import se.kth.iv1201.recruitment.repository.PersonRepository;

@Controller
public class UserPageController {

    private final PersonRepository personRepository;
    private final ApplicationRepository applicationRepository;

    public UserPageController(PersonRepository personRepository, ApplicationRepository applicationRepository) {
        this.personRepository = personRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/userPage")
    public String userPage(Model model) {
        // Get logged-in user's username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Find person by username
        Person person = personRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean hasSubmittedApplication = applicationRepository.existsByPersonPersonId(person.getPersonId());
        model.addAttribute("hasSubmittedApplication", hasSubmittedApplication);

        return "userPage";
    }
}