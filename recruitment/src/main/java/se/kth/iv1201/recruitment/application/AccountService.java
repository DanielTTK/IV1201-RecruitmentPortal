/**
 * Handles register/login-related logic.
 *
 * Here the “rules” are kept (unique username, password hashing etc) and log important events
 * PW hashing (HGP 7) and logging (HGP 9) done.
 *
 */


package se.kth.iv1201.recruitment.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import se.kth.iv1201.recruitment.application.error.UsernameTakenException;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser( String firstName, String lastName, String username, String personNumber,
                                String email, String rawPassword
    ) {
        System.out.println("DEBUG: AccountService.registerUser called, username=" + username);
        log.info("Register attempt: username={}", username);
        
        if (personRepository.existsByUsernameIgnoreCase(username)) {
            log.info("Register failed: username_taken username={}", username);
            throw new UsernameTakenException(username);
        }

        Person person = new Person();
        person.setName(firstName);
        person.setSurname(lastName);
        person.setUsername(username);
        person.setPnr(personNumber);
        person.setEmail(email);
        person.setPassword(passwordEncoder.encode(rawPassword));
        person.setRoleId(2); // applicant

        Person saved = personRepository.save(person); // persists the user in the database

        log.info("Register success: username={} personId={}", username, saved.getPersonId());
    } 
}