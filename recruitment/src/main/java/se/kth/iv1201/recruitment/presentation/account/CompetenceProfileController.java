package se.kth.iv1201.recruitment.presentation.account;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/competence")
    public String competence(@Valid @ModelAttribute("competenceProfile") CompetenceProfile form,
            BindingResult bindingResult,
            @RequestParam(value = "addDateRow", required = false) String addDateRow,
            @RequestParam(value = "addExperienceRow", required = false) String addExperienceRow,
            String submitted,
            Model model) {

                System.out.println("Date ranges" + form.getDateRanges());
                System.out.println("Experiences" + form.getExperiences());
        if (addDateRow != null) {
            form.getDateRanges().add(new DateRange());
            model.addAttribute("competenceProfile", form);
            return "competenceProfile";
        }

        if (addExperienceRow != null) {
            form.getExperiences().add(new Experiences());
            model.addAttribute("competenceProfile", form);
            return "competenceProfile";
        }

        if (submitted != null) {
            return "competenceDone";
        }

        if (bindingResult.hasErrors()) {
            return "competenceProfile";
        }

        return "competenceProfile";
    }
}
