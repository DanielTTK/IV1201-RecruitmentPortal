package se.kth.iv1201.recruitment.presentation.account;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import se.kth.iv1201.recruitment.application.error.ApplicationAlreadySubmitted;
import se.kth.iv1201.recruitment.application.ApplicationService;

/**
 * Controller for competence profile controller. Send correct info from
 * application to database.
 * 
 * The form state is stored temporarily in the HTTP session using
 * @SessionAttributes, until the application is either submitted
 * or cancelled.
 * 
 */

@Controller
@SessionAttributes("competenceProfile") 
public class CompetenceProfileController {

    private final ApplicationService applicationService;

    /**
     * Constructs the CompetenceProfileController with the required ApplicationService dependency. The ApplicationService 
     * is used to handle the business logic of submitting the competence profile application.
     * 
     * @param applicationService
     */
    public CompetenceProfileController(ApplicationService applicationService) {
    this.applicationService = applicationService;
    }


    /**
     * Initializes the competence profile form with default values. This method is called before any request handling method in this 
     * controller, and ensures that there is a competence profile form object in the session for the user to fill out. The form is 
     * initialized with one empty date range and one empty experience to allow the user to start filling out their competence profile 
     * immediately.
     * 
     * @return the initialized competence profile form with default values
     */

    @ModelAttribute("competenceProfile")
    public CompetenceProfileForm initializeForm()  {
        CompetenceProfileForm form = new CompetenceProfileForm();
        form.getDateRanges().add(new DateRange());
        form.getExperiences().add(new Experiences());
        return form;
    }
    @GetMapping("/competenceProfile")
    public String competenceProfile() {
        return "competenceProfile";
    }

    /**
     * Handles competence profile form submission
     * Returns different views depending on the input.
     * 
     * @return If button for "+" next to competences are pressed, a new row of competences should appear.
     * @return If button for "+" next to dates are pressed, a new row of dates should appear.
     * @return Returning to the review page, if all required parts are filled out, and review button is pressed.
     * @return same page again if errors appear, including the errors.
     */
    @PostMapping("/competence")
    public String competence(@Valid @ModelAttribute("competenceProfile") CompetenceProfileForm form,
            BindingResult bindingResult,
            @RequestParam(value = "addDateRow", required = false) String addDateRow,
            @RequestParam(value = "addExperienceRow", required = false) String addExperienceRow,
            @RequestParam(value = "review", required = false) String review
            ) {


        if (bindingResult.hasErrors()) {
            return "competenceProfile";
        }

        if (addDateRow != null) {
            form.getDateRanges().add(new DateRange());
            return "competenceProfile";
        } else if (addExperienceRow != null) {
            form.getExperiences().add(new Experiences());
            return "competenceProfile";
        } 
        return "competenceReview";
    }


    /**
     * Cancels the competence profile form submission process by clearing the session attributes and returning 
     * to the competence profile page.
     * 
     * @param sessionStatus used to clear the session attributes related to the competence profile form
     * @return a redirect to the competence profile page
     */
    @GetMapping("/competenceProfile/cancel")
    public String cancel(SessionStatus sessionStatus) {
        sessionStatus.setComplete(); 
        return "/competenceProfile";
    }


    /**
     * Handles the review of the competence profile form. This method is called when the user clicks the "Review"
     * button on the competence profile form.
     * 
     * @return the view name for the competence profile review page, where the user can see a summary of their input and 
     * confirm submission.
     */
    @GetMapping("/competenceProfile/review")
    public String review() {
        return "competenceReview";
    }

    /**
     *  Handles the final submission of the competence profile form. It calls the application service to submit the application, 
     *  and handles any exceptions that may occur during submission.
     * 
     * @param authentication: Authenticated user submitting the application
     * @param form: The competence profile stored in the session
     * @param sessionStatus: used to clear the session after successful submission
     * @param model: used to pass error messages to the view
     * @return: Returns to competence_success view if the submission is successful, otherwise review view.
     */
    @PostMapping("/competenceProfile/submit")
        public String submit(Authentication authentication, 
                        @ModelAttribute("competenceProfile") CompetenceProfileForm form,
                        SessionStatus sessionStatus, Model model) {

    try {
        applicationService.submitApplication(authentication.getName(), form);
        sessionStatus.setComplete(); // clear session after submission => clear old data
        return "competence_success";

        } catch (ApplicationAlreadySubmitted e) {

            model.addAttribute("submitError", e.getMessage());
            return "competenceReview";

        } catch (Exception e) {

            model.addAttribute("submitError",
                    "Could not submit the application. Please try again later.");
            return "competenceReview";
        }
    }
}
