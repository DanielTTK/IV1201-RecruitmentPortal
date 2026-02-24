package se.kth.iv1201.recruitment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.iv1201.recruitment.domain.Person;

/**
 * Repository for {@link se.kth.iv1201.recruitment.domain.Person}.
 *
 * Used by registration/login flows to look up users and perform uniqueness checks.
 *
 */



public interface PersonRepository extends JpaRepository<Person, Integer> {

    /**
     * Finds a person by username (case-insensitive).
     *
     * @param username The username to search for.
     * @return The matching person, or empty if none exists.
     */

    Optional<Person> findByUsernameIgnoreCase(String username);



    /**
     * Finds a person by email (case-insensitive).
     *
     * @param email The email address to search for.
     * @return The matching person, or empty if none exists.
     */
    Optional<Person> findByEmailIgnoreCase(String email);


    /**
     * Checks if a username already exists. Only for use in field error logic!
     *
     * @param username The username to check.
     * @return True if the username exists, otherwise false.
     */

    boolean existsByUsernameIgnoreCase(String username);


    
    /**
     * Checks if an email already exists. Only for use in field error logic!
     *
     * @param email The email address to check.
     * @return True if the email exists, otherwise false.
     */
    boolean existsByEmailIgnoreCase(String email);

    
    /**
     * Checks if a person number already exists. Only for use in field error logic!
     *
     * @param pnr The person number to check.
     * @return True if the person number exists, otherwise false.
     */
    boolean existsByPnr(String pnr);


    
}