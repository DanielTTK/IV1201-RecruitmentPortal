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
import java.time.LocalDate;
import java.util.Objects;

/**
 * Corresponds to the {@code availability} table.
 *
 * Availability period for a person, part of the filled in application.
 * Many {@code availability} rows can reference the same person.
 */


@Entity
@Table(name = "availability")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private Integer availabilityId;


    /**
     * The person this availability belongs to.
     * Mapped to {@code availability.person_id} (NOT NULL in DB).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;


    /**
     * Required by JPA, should not be used directly.
     */
    public Availability() {
    }

    public Integer getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Integer availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }


    /**
     * Equality is based on the primary key (availabilityId).
     *
     * @param o The object to compare with.
     * @return {@code true} if both objects represent the same persisted row.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability other)) return false;
        return availabilityId != null && Objects.equals(availabilityId, other.availabilityId);
    }

    @Override
    public String toString() {
        Integer personId = (person == null) ? null : person.getPersonId();
        return "Availability{availabilityId=" + availabilityId + ", personId=" + personId + ", fromDate=" + fromDate + ", toDate=" + toDate + "}";
    }
}