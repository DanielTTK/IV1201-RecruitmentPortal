package se.kth.iv1201.recruitment.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.iv1201.recruitment.domain.Application;

/**
 * Repository for {@link se.kth.iv1201.recruitment.domain.Application}.
 *
 * Used by UC 5.3-5.5 to store and query applications for an applicant.
 *
 */



public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    /**
     * Returns all applications for a person.
     *
     * @param personId The person's ID.
     * @return All applications belonging to the person.
     */

    List<Application> findAllByPersonPersonId(Integer personId);



    /**
     * Returns an application for a person if it exists.
     *
     * @param personId The person's ID.
     * @return The person's application, or empty if none exists.
     */
    Optional<Application> findByPersonPersonId(Integer personId);



    /**
     * Checks whether a person already has an application.
     *
     * @param personId The person's ID.
     * @return True if an application exists for the person, otherwise false.
     */
    boolean existsByPersonPersonId(Integer personId);
}