package se.kth.iv1201.recruitment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

    // Since we have two different login portals with restricted access, 
    // we need to create multiple filter chains which will mean to order them. 

    // Since we have two different login portals with restricted access, 
    // we need to create multiple filter chains which will mean to order them. 
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

                .failureUrl("/loginAdmin?error") //Throw any {param.error} included in the loginAdmin template
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
                                 "/loginAdmin").permitAll() // allow everyone, including non-authenticated to access the login portal.
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
                .failureUrl("/loginPage?error") 
            )
            .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/home")
        );
        return http.build();
    } 
}
