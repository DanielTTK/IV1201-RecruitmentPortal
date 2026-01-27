package se.kth.iv1201.recruitment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) //Just let everything through for now so no login required

            .formLogin(Customizer.withDefaults());
        

        http.httpBasic(httpBasic -> httpBasic.disable());



        http.csrf(csrf -> csrf.disable()); //re-enable ts later when we implement real auth.

        return http.build();
    }
}