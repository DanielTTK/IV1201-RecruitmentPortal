package se.kth.iv1201.recruitment.presentation.account;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/adminPage")
    public String adminPage(Model model) {
        model.addAttribute("applications", List.of());
        return "adminPage";
    }
}
