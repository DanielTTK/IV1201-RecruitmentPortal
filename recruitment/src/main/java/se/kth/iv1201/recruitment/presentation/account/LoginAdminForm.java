package se.kth.iv1201.recruitment.presentation.account;

import jakarta.validation.constraints.NotBlank;

public class LoginAdminForm {

    @NotBlank
    private String usernameAdmin;

    @NotBlank
    private String passwordAdmin;

    public String getUsernameAdmin(){
        return usernameAdmin;
    }

    public void setUsernameAdmin(String usernameAdmin){
        this.usernameAdmin=usernameAdmin;
    }

       public String getPasswordAdmin(){
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin){
        this.passwordAdmin=passwordAdmin;
    }
    
}
