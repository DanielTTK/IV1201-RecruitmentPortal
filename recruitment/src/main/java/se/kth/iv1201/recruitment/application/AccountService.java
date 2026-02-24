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

import jakarta.transaction.Transactional;
import se.kth.iv1201.recruitment.application.error.EmailTakenException;
import se.kth.iv1201.recruitment.application.error.PersonNumberTakenException;
import se.kth.iv1201.recruitment.application.error.UsernameTakenException;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;


/**
 * Service layer responsible for handling account-related business logic.
 *
 * <p>This service manages user registration, including validation of unique
 * fields (username and email), password encryption, and persistence of
 * {@link Person} entities.</p>
 *
 * All write operations are executed within a transactional context
 */
@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs an AccountService with required dependencies.
     *
     * @param personRepository repository used to access and persist {@link Person} entities
     * @param passwordEncoder encoder used to securely hash user passwords before storing them
     */
    public AccountService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     * This method performs the following steps:
     * - Validates that the email and username is not already taken,
     * - Encrypts the provided raw password,
     * - Creates and persists a new {@link Person} entity
     * If validation fails, a domain-specific runtime exception is thrown. 
     *
     * @param firstName     the user's first name
     * @param lastName      the user's last name
     * @param username      the desired username (must be unique, case-insensitive)
     * @param personNumber  the user's personal identification number
     * @param email         the user's email address (must be unique, case-insensitive)
     * @param rawPassword   un-encrypted, raw password provided during registration
     */
    @Transactional
    public void registerUser( String firstName, String lastName, String username, String personNumber,
                                String email, String rawPassword
    ) {
        //System.out.println("DEBUG: AccountService.registerUser called, username=" + username);
        log.info("Register attempt: username={}", username);
        
        if (personRepository.existsByUsernameIgnoreCase(username)) {
            log.info("Register failed: username_taken username={}", username);
            throw new UsernameTakenException(username);
        }

        //Pnr normalized to match DB format
        String digits = personNumber.replaceAll("\\D", "");
        String normalizedPnr = digits.substring(0, 8) + "-" + digits.substring(8);

        if (personRepository.existsByPnr(normalizedPnr)) {
            log.info("Register failed: personnumber_taken personNumber={}", normalizedPnr);
            throw new PersonNumberTakenException();

        }
        if (personRepository.existsByEmailIgnoreCase(email)) {
            log.info("Register failed: email_taken email={}", email);
            throw new EmailTakenException(email);
        }
        
        Person person = new Person();
        person.setName(firstName);
        person.setSurname(lastName);
        person.setUsername(username);
        person.setPnr(normalizedPnr);
        person.setEmail(email);
        person.setPassword(passwordEncoder.encode(rawPassword));
        person.setRoleId(2); // applicant

        Person saved = personRepository.save(person); // persists the user in the database

        log.info("Register success: username={} personId={}", username, saved.getPersonId());
    } 

    @Transactional
    public void completeLegacyUser(Integer personId, String firstName, String lastName, String username, String personNumber, String email, String rawPassword) 
    {

        Person person = personRepository.findById(personId).orElseThrow(() -> new IllegalArgumentException("Legacy user not found"));

        if (!person.isLegacy()) {
            return;
        }

        String digits = personNumber.replaceAll("\\D", "");
        String normalizedPnr = digits.substring(0, 8) + "-" + digits.substring(8);

        Integer roleId = person.getRoleId(); //preserve recruiter/applicant role

        person.setName(firstName);
        person.setSurname(lastName);
        person.setUsername(username);
        person.setEmail(email);
        person.setPnr(normalizedPnr);
        person.setPassword(passwordEncoder.encode(rawPassword));
        person.setRoleId(roleId);
        person.setLegacy(false);

        personRepository.save(person);
    }
}