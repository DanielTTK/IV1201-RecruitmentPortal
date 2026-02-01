package se.kth.iv1201.recruitment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.iv1201.recruitment.domain.Person;

/**
 * DB access for person.
 * Used by register/login flows (lookups + uniqueness check).
 */

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByUsername(String username); //returns given person if exists


    boolean existsByUsername(String username); //true if username is taken
}