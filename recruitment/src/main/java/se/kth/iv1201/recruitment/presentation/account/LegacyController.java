package se.kth.iv1201.recruitment.presentation.account;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import se.kth.iv1201.recruitment.application.ResendEmailService;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for handling legacy user verification. This controller manages the flow for users who has the is_legacy 
 * flag set to true during login. 
 * 
 * It provides endpoints for starting the legacy verification process, verifying the OTP code, and canceling.
 */
@Controller
@RequestMapping("/legacy")
public class LegacyController {

    private static final Logger log = LoggerFactory.getLogger(LegacyController.class);

    private static final String LEGACY_PERSON_ID = "LEGACY_PERSON_ID";
    private static final String LEGACY_OTP = "LEGACY_OTP";

    private final PersonRepository repo;
    private final ResendEmailService resendEmailService;


    
    /**
     * Creates a new LegacyController.
     * 
     * @param repo the repository to look up legacy user information
     * @param resendEmailService the service to send OTP emails to legacy users
     */
    public LegacyController(PersonRepository repo, ResendEmailService resendEmailService) {
        this.repo = repo;
        this.resendEmailService = resendEmailService;
    }


    /** 
     * Starts the legacy verification process. It checks if the provided identifier (username or email) belongs to a legacy user,
     * generates an OTP code, stores it in the session, and sends it to the user's email address.
     * 
     * @param identifier the username or email entered by the user during login
     * @param session the session to store legacy user information and OTP code
     */
    @GetMapping("/start")
    public String start(@RequestParam("identifier") String identifier,
                        HttpSession session) {

        log.info("LEGACY_OTP_START identifier_present={}", identifier != null && !identifier.isBlank());

        Person person = repo.findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier)
                .orElse(null);

        if (person == null || !person.isLegacy()) {
            log.warn("LEGACY_OTP_START_REJECTED reason=not_legacy_or_missing");
            return "redirect:/loginPage?error";
        }

        if (person.getEmail() == null || person.getEmail().isBlank()) {
            log.warn("LEGACY_OTP_START_REJECTED personId={} reason=missing_email", person.getPersonId());
            return "redirect:/loginPage?error";
        }

        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        session.setAttribute(LEGACY_PERSON_ID, person.getPersonId());
        session.setAttribute(LEGACY_OTP, otp);

        resendEmailService.sendOtp(person.getEmail(), otp);
        log.info("LEGACY_OTP_SENT personId={}", person.getPersonId());

        return "legacy_otp";
    }

    /**
     * Verifies the OTP code entered by the user. If the code is correct, the user is redirected 
     * to the registration page to complete their account creation.
     * 
     * @param code the OTP code entered by the user
     * @param session the session to retrieve the expected OTP and legacy user information
     * @param model the model to pass error information to the view if the OTP is incorrect    
     * @return
     */
    @PostMapping("/verify")
    public String verify(@RequestParam("code") String code,
                        HttpSession session,
                        Model model) {

        String expected = (String) session.getAttribute(LEGACY_OTP);
        Integer personId = (Integer) session.getAttribute(LEGACY_PERSON_ID);

        if (expected == null || code == null || !code.equals(expected)) {
            log.warn("LEGACY_OTP_VERIFY_FAILURE personId={}", personId);
            model.addAttribute("error", true);
            return "legacy_otp";
        }

        log.info("LEGACY_OTP_VERIFY_SUCCESS personId={}", personId);
        return "redirect:/register";
    }


    /**
     * Cancels the legacy verification process by clearing the session attributes and redirecting to the login page.
     * 
     * @param session the session to clear legacy user information and OTP code
     * @return a redirect to the login page
     */
    @GetMapping("/cancel")
    public String cancel(HttpSession session) {
        Integer personId = (Integer) session.getAttribute(LEGACY_PERSON_ID);
        log.info("LEGACY_OTP_CANCEL personId={}", personId);

        session.removeAttribute(LEGACY_PERSON_ID);
        session.removeAttribute(LEGACY_OTP);
        return "redirect:/loginPage";
    }
}