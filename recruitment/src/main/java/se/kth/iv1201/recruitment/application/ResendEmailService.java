package se.kth.iv1201.recruitment.application;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for sending OTP emails to legacy users. It uses the Resend email service to send emails.
 * The service is configured with an API key and a sender email address, which are injected from
 * application properties.
 * 
 */


@Service
public class ResendEmailService {

    private final Resend resend;
    private final String from;


    /**
     * Creates a new ResendEmailService with the given API key and sender email address.
     * 
     * @param apiKey the API key for the Resend email service, injected from application properties
     * @param from the sender email address for OTP emails, injected from application properties
     * @throws RuntimeException if the Resend client fails to initialize
     */
    public ResendEmailService(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from}") String from
    ) {
        this.resend = new Resend(apiKey);
        this.from = from;
    }

    /** 
     * Sends an OTP email to the specified recipient.
     * 
     * @param toEmail the email address of the recipient
     * @param otp the OTP code to be sent in the email
     */
    public void sendOtp(String toEmail, String otp) {
        CreateEmailOptions params = CreateEmailOptions.builder().from(from).to(toEmail)
                .subject("Your verification code")
                .html("<p>Your verification code is: <strong>" + otp + "</strong></p>")
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}