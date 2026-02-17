package se.kth.iv1201.recruitment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents one row in the {@code competence} table.
 *
 * <p>
 * A competence is a named skill/area (e.g. "Restaurant", "Cashier") that can be linked to
 * applicants through {@link CompetenceProfile} rows.
 * </p>
 */


@Entity
@Table(name = "competence")
public class Competence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competence_id")
    private Integer competenceId;


    @Column(name = "name", nullable = false)
    private String name;


    /**
     * All competence profiles that reference this competence.
     *
     * <p>
     * One competence can be referenced by many {@link CompetenceProfile} rows (one per person).
     * </p>
     */
    @OneToMany(mappedBy = "competence", fetch = FetchType.LAZY)
    private List<CompetenceProfile> competenceProfiles = new ArrayList<>();


    /**
     * Required by JPA, should not be used directly.
     */ 
    public Competence() {
    }

    public Integer getCompetenceId() {
        return competenceId;
    }

    public void setCompetenceId(Integer competenceId) {
        this.competenceId = competenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CompetenceProfile> getCompetenceProfiles() {
        return competenceProfiles;
    }


    /**
     * Equality is based on the primary key (competenceId).
     *
     * @param o The object to compare with.
     * @return {@code true} if both objects represent the same persisted row.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Competence other)) return false;
        return competenceId != null && Objects.equals(competenceId, other.competenceId);
    }

    @Override
    public String toString() {
        return "Competence{competenceId=" + competenceId + ", name=" + name + "}";
    }
}