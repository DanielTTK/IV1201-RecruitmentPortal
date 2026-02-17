package se.kth.iv1201.recruitment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.iv1201.recruitment.domain.Availability;

/**
 * Repository for {@link se.kth.iv1201.recruitment.domain.Availability}.
 *
 * Used by UC 5.3 (Submit application) to store and fetch an applicant's available periods.
 * Availabilities are linked to a person via {@code person_id}.
 */



public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {

    /**
     * Returns all availability periods for a person.
     *
     * @param personId The person's ID.
     * @return All availability rows for the person.
     */

    List<Availability> findAllByPersonPersonId(Integer personId);



    /**
     * Checks whether a person has any availability rows.
     *
     * @param personId The person's ID.
     * @return True if at least one availability exists, otherwise false.
     */
    boolean existsByPersonPersonId(Integer personId);



}