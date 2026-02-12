package se.kth.iv1201.recruitment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.iv1201.recruitment.domain.Person;

/**
 * DB access for person.
 * Used by register/login flows (lookups + uniqueness check).
 */

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByUsernameIgnoreCase(String username); //returns given person if exists
    Optional<Person> findByEmailIgnoreCase(String email);


    boolean existsByUsernameIgnoreCase(String username); //only for use in field error logic!!
    boolean existsByEmailIgnoreCase(String email); //only for use in field error logic!!


    
}