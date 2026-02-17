package se.kth.iv1201.recruitment.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.iv1201.recruitment.domain.CompetenceProfile;

/**
 * Repository for {@link se.kth.iv1201.recruitment.domain.CompetenceProfile}.
 *
 * Used by UC 5.3 (Submit application) to store and query an applicant's competences
 * + years of experience. The DB has {@code (person_id, competence_id)} as UNIQUE.
 * 
 */



public interface CompetenceProfileRepository extends JpaRepository<CompetenceProfile, Integer> {

    /**
     * Returns all competence profiles for a person.
     *
     * @param personId The person's ID.
     * @return All competence profiles for the person.
     */

    List<CompetenceProfile> findAllByPersonPersonId(Integer personId);



    /**
     * Finds a competence profile for a specific (person, competence) pair.
     *
     * @param personId The person's ID.
     * @param competenceId The competence ID.
     * @return The matching competence profile, or empty if none exists.
     */

    Optional<CompetenceProfile> findByPersonPersonIdAndCompetenceCompetenceId(Integer personId, Integer competenceId);



    /**
     * Checks whether a competence profile already exists for a (person, competence) pair.
     *
     * @param personId The person's ID.
     * @param competenceId The competence ID.
     * @return True if it exists, otherwise false.
     */
    boolean existsByPersonPersonIdAndCompetenceCompetenceId(Integer personId, Integer competenceId);
}