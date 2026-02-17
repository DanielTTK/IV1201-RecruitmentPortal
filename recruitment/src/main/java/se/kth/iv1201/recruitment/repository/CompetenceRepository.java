package se.kth.iv1201.recruitment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.iv1201.recruitment.domain.Competence;

/**
 * Repository for {@link se.kth.iv1201.recruitment.domain.Competence}.
 *
 * Used by UC 5.3 to list available competences and resolve competence IDs/names.
 * The database enforces unique competence names.
 */



public interface CompetenceRepository extends JpaRepository<Competence, Integer> {

    /**
     * Finds a competence by name (case-insensitive).
     *
     * @param name The competence name.
     * @return The matching competence, or empty if none exists.
     */
    Optional<Competence> findByNameIgnoreCase(String name);



    /**
     * Checks if a competence name exists.
     *
     * @param name The competence name to check.
     * @return True if the competence exists, otherwise false.
     */
    boolean existsByNameIgnoreCase(String name);
}