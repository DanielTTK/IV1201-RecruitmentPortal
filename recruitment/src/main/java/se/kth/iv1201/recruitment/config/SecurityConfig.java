/**
 * Security setup for the project.
 *
 * Provides password encoder for hashing (HGP 7). Right now (29/1) it's configured
 * to not block register/login pages, later it may be configured to enforce tighter access rules.
 */

package se.kth.iv1201.recruitment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register","/home","/css/**",
                                "/loginPage","/loginAdmin").permitAll() // permit everyone to access and view these pages
                .requestMatchers("/admin/**", "/adminPage").hasRole("RECRUITER") //restrict this pages for the recruiter
                .requestMatchers("/competenceProfile", "/competence") 
                    .hasRole("APPLICANT") // only allow the applicant to access thesse pages
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/loginPage")
                .loginProcessingUrl("/login") // Post endpoint for spring security config
                .successHandler((request, response, authentication) -> {

                    boolean isRecruiter = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"));

                    boolean isApplicant = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_APPLICANT"));

                    String portal = request.getParameter("portal");

                    // If trying to login from admin portal as a user, and vice versa for user portal, as an admin.
                    if ("admin".equals(portal) && !isRecruiter) {
                        request.getSession().invalidate();
                        response.sendRedirect("/loginAdmin?error=unauthorized");
                        return;
                    }
                    if ("user".equals(portal) && !isApplicant) {
                        request.getSession().invalidate();
                        response.sendRedirect("/loginPage?error=unauthorized");
                        return;
                    }

                    // Normal redirection for login
                    if (isRecruiter) {
                        response.sendRedirect("/adminPage");
                    } else if (isApplicant) {
                        response.sendRedirect("/competenceProfile");
                    } else {
                        response.sendRedirect("/home");
                    }
                })
                .failureUrl("/loginPage?error") // display any {param.error} that is included in the given thymeleaf template.
            );

        return http.build();
    }
}