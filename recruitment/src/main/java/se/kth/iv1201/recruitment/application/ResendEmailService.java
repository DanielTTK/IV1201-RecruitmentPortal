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

    public ResendEmailService(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from}") String from
    ) {
        this.resend = new Resend(apiKey);
        this.from = from;
    }

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