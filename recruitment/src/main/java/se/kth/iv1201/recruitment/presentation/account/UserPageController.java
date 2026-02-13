package se.kth.iv1201.recruitment.presentation.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/userPage")
    public String userPage (){
        return "userPage";
    }
    //TODO - connect users applications from database so these can be shown 

}
