package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;


@Controller
public class LoginAdminController {

    @GetMapping("/loginAdmin")
    public String loginAdmin(Model model) {
        model.addAttribute("loginAdmin", new LoginForm());
        return "loginAdmin";
    }
}
