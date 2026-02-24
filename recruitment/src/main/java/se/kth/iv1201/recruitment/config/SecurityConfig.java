package se.kth.iv1201.recruitment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import se.kth.iv1201.recruitment.repository.PersonRepository;

/**
 * Security configuration for the application. Defines two security filter chains:
 * - AdminSecurity: Protects admin pages, only allows users with RECRUITER role.
 * - UserSecurity: Protects user pages, only allows users with APPLICANT role.
 *
 * Also defines a BCryptPasswordEncoder bean for hashing passwords.
 *
 * The admin login flow uses a custom success handler to check the user's roles after authentication,
 * and a failure handler to redirect legacy users to the OTP flow.
 *
 * The user login flow also uses custom handlers to enforce access based on role and handle legacy users.
 *
 * Logout is configured to invalidate the session and redirect to the home page.
 *
 * @see se.kth.iv1201.recruitment.presentation.account.RegisterController for the registration flow that creates users in the database.
 * @see se.kth.iv1201.recruitment.presentation.account.LegacyController for handling legacy user OTP verification.
 */
@Configuration
public class SecurityConfig {

    private final PersonRepository personRepository;

    public SecurityConfig(PersonRepository personRepository) {
    this.personRepository = personRepository;
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain for the admin login portal. Only allows users with the RECRUITER role to access the admin pages.
     * @param http the HttpSecurity to configure
     * @return the security filter chain for the admin login portal
     * @throws Exception
    */
    @Bean
    @Order(1) // handles the admin login portal, and is evaluated first
    SecurityFilterChain adminSecurity(HttpSecurity http) throws Exception {

        http
            .securityMatcher( "/admin/**", "/adminPage", "/admin/login") 
            .authorizeHttpRequests(auth -> auth
                .anyRequest().hasRole("RECRUITER") // only allow the recruiter to access these pages, after the authentication
            )
            .formLogin(form -> form
                .loginPage("/loginAdmin") 
                .loginProcessingUrl("/admin/login") 
                .successHandler((request, response, authentication) -> { // Using the successHandler in order to separate authentication from authorization

                boolean isRecruiter = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));

                if (!isRecruiter) {
                    request.getSession().invalidate();
                    response.sendRedirect("/loginAdmin?error");
                    return;
                }

                response.sendRedirect("/adminPage");
            })


            .failureHandler((httpRequest, httpResponse, authException) -> {
                String identifier = httpRequest.getParameter("username");

                boolean legacy = identifier != null && !identifier.isBlank() &&
                        personRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier)
                                .map(p -> p.isLegacy())
                                .orElse(false);

                String target = legacy
                        ? "/legacy/start?identifier=" + java.net.URLEncoder.encode(identifier, java.nio.charset.StandardCharsets.UTF_8)
                        : "/loginAdmin?error";

                httpResponse.sendRedirect(target);
            })
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
            );

        return http.build();
    }

    @Bean
    @Order(2) //handled after (fallback) by default
    SecurityFilterChain userSecurity(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register","/home","/css/**","/loginPage",
                                 "/loginAdmin","/legacy/**").permitAll() // allow everyone, including non-authenticated to access the login portal.
                .requestMatchers("/competenceProfile", "/competence", "/userPage")
                    .hasRole("APPLICANT") // Only allow the applicant to access these pages that are being requested.
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/loginPage")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> { // Using the successHandler in order to separate authentication from authorization

                boolean isApplicant = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_APPLICANT"));

                if (!isApplicant) {
                    request.getSession().invalidate(); // 
                    response.sendRedirect("/loginPage?error");// display any {param.error} that is included in the given thymeleaf template.
                    return;
                }
                response.sendRedirect("/competenceProfile");
                })
                .failureHandler((req, res, ex) -> {
                    String identifier = req.getParameter("username"); // may be username or email
                    var p = (identifier == null || identifier.isBlank())
                            ? null
                            : personRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(identifier, identifier).orElse(null);

                    if (p != null && p.isLegacy()) {
                        String enc = java.net.URLEncoder.encode(identifier, java.nio.charset.StandardCharsets.UTF_8);
                        res.sendRedirect("/legacy/start?identifier=" + enc);
                    } else {
                        res.sendRedirect("/loginPage?error");
                    }
                })
            )
            .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/home")
        );
        return http.build();
    } 

}
