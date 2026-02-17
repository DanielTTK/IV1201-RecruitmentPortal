package se.kth.iv1201.recruitment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Represents a row in the {@code competence_profile} table.
 *
 * <p>
 * A competence profile connects one {@link Person} to one {@link Competence} with a number of
 * years of experience.
 * </p>
 *
 * <p>
 * The database enforces uniqueness for a person/competence pair via a UNIQUE index on
 * {@code (person_id, competence_id)}.
 * </p>
 */


@Entity
@Table(name = "competence_profile")
public class CompetenceProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competence_profile_id")
    private Integer competenceProfileId;


    /**
     * The person that owns this competence profile row.
     *
     * <p>
     * Many {@code competence_profile} rows can reference the same person (one per competence).
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;



    /**
     * The competence associated with this profile row.
     *
     * <p>
     * Many {@code competence_profile} rows can reference the same competence (for different persons).
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "competence_id", nullable = false)
    private Competence competence;



    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;



    /**
     * Required by JPA, should not be used directly.
     */
    public CompetenceProfile() {
    }

    public Integer getCompetenceProfileId() {
        return competenceProfileId;
    }

    public void setCompetenceProfileId(Integer competenceProfileId) {
        this.competenceProfileId = competenceProfileId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Competence getCompetence() {
        return competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }


    /**
     * Equality is based on the primary key (competenceProfileId).
     *
     * @param o The object to compare with.
     * @return {@code true} if both objects represent the same persisted row.
     */
    @Override
    public boolean equals(Object o) {  
        if (this == o) return true;
        if (!(o instanceof CompetenceProfile other)) return false;
        return competenceProfileId != null && Objects.equals(competenceProfileId, other.competenceProfileId);
    }

    @Override
    public String toString() {
        Integer personId = (person != null) ? person.getPersonId() : null;
        Integer competenceId = (competence != null) ? competence.getCompetenceId() : null;
        return "CompetenceProfile{competenceProfileId=" + competenceProfileId + ", personId=" + personId + ", competenceId=" + competenceId + ", yearsOfExperience=" + yearsOfExperience + "}";
    }
}