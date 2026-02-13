package se.kth.iv1201.recruitment.presentation.account;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for competence profile controller. Send correct info from
 * application to database.
 */

@Controller
public class CompetenceProfileController {

    @GetMapping("/competenceProfile")
    public String competenceProfile(Model model) {
        CompetenceProfile form = new CompetenceProfile();
        form.getDateRanges().add(new DateRange());
        form.getExperiences().add(new Experiences());
        model.addAttribute("competenceProfile", form);
        return "competenceProfile";
    }

    /**
     * Handles competence profile form submission
     * 
     * Returns different views depending on the input.
     * 
     * @return If button for "+" next to competences are pressed, a new row of
     *         competences
     *         should appear.
     * @return If button for "+" next to dates are pressed, a new row of dates
     *         should
     *         appear.
     * @return Returning a page for submission success, if all required parts are
     *         filled
     *         out, and submit button is pressed.
     * @return same page agian if errors appear, including the errors.
     */
    @PostMapping("/competence")
    public String competence(@Valid @ModelAttribute("competenceProfile") CompetenceProfile form,
            BindingResult bindingResult,
            @RequestParam(value = "addDateRow", required = false) String addDateRow,
            @RequestParam(value = "addExperienceRow", required = false) String addExperienceRow,
            @RequestParam(value = "submitted", required = false) String submitted,
            Model model) {

        System.out.println("Date ranges" + form.getDateRanges());
        System.out.println("Experiences" + form.getExperiences());

        if (bindingResult.hasErrors()) {
            return "competenceProfile";
        }

        else if (addDateRow != null) {
            form.getDateRanges().add(new DateRange());
            model.addAttribute("competenceProfile", form);
            return "competenceProfile";
        } else if (addExperienceRow != null) {
            form.getExperiences().add(new Experiences());
            model.addAttribute("competenceProfile", form);
            return "competenceProfile";
        } else if (submitted != null) {
            return "competence_success";
        }

        return "competenceProfile";
    }
}
