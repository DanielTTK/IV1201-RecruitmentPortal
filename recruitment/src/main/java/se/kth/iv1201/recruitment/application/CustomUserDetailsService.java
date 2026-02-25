package se.kth.iv1201.recruitment.application;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * This service is responsible for loading user information from the database
 * during the authentication process.
 * 
 * It retrieves a Person entity and converts it into a Spring Security
 * UserDetails object used for authentication and authorization.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;
    
    /**
     * Creates a new CustomUserDetailsService.
     * @param personRepository repository used to retrieve Person entities
     */
    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * This method is called automatically by Spring Security when a user
     * attempts to log in.
     *
     * It looks up the user by username (or email), checks if the user is a legacy user, 
     * and if not, it builds a UserDetails object with the user's information and roles. 
     *
     * @param username the username provided during login: it loads a user by username during authentication.
     * @return a UserDetails object used by Spring Security
     * @throws UsernameNotFoundException if the user does not exist or does not have a password
     */
    @Override 
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {     

    Person person = personRepository
            .findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // internal error

        if (person.isLegacy()) {
        throw new UsernameNotFoundException("Legacy user");
        }

        String role = mapRole(person.getRoleId());
        return User.builder() // Spring security's internal user model: we build the object to contain the following, to authenticate it with the given requirements:
                .username(person.getUsername())
                .password(person.getPassword()) 
                .roles(role) // Spring adds ROLE_ prefix automatically, used for the SecurityConfig when authorizing the user.
                .build();
    }
    /**
     * Maps internal role identifiers to Spring Security role names.
     * @param roleId the internal role identifier stored in the database
     * @return the corresponding Spring Security role name
     */
    private String mapRole(Integer roleId) {
        if (roleId == 1) {
            return "RECRUITER"; // we use these labels for spring security config file
        } else {
            return "APPLICANT";
        }
    }
}
