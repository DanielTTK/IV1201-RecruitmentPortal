package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class LoginAdminController {

    @GetMapping("/loginAdmin")
    public String loginAdmin(Model model) {
        model.addAttribute("loginAdmin", new LoginForm());
        return "loginAdmin";
    }

    // Had to comment this out so it wouldn't interfere with spring security's Post endpoint
    /*@PostMapping("/loginAdmin")
    public String loginAdminSubmit(@Valid @ModelAttribute LoginForm form,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "loginAdmin";
        }

        // TODO: connect to application to be able to check with db 
        return "redirect:/adminPage";
    }*/
}
