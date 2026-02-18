package se.kth.iv1201.recruitment.application;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;

// Service layer used for login to identify the login credentials for the recruiter, and the applicant.
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override 
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Person person = personRepository.findByUsernameIgnoreCase(username) // we search up the person from the database.
                .orElseThrow(() -> 
                new UsernameNotFoundException("User not found")); // internal error

        if (person.getPassword() == null) { // Temporarily blocking the users that does not have a password
            throw new UsernameNotFoundException("User has no password"); // internal error
        }

        String role = mapRole(person.getRoleId());
        return User.builder() // Spring security's internal user model: we build the object to contain the following, to authenticate it with the given requirements:
                .username(person.getUsername())
                .password(person.getPassword()) 
                .roles(role) // Spring adds ROLE_ prefix automatically, used for the SecurityConfig when authorizing the user.
                .build();
    }

    private String mapRole(Integer roleId) { // mapping the roleIds to respective user.
        if (roleId == 1) {
            return "RECRUITER"; // we use these labels for spring security config file
        } else {
            return "APPLICANT";
        }
    }
}
