package se.kth.iv1201.recruitment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Simple unit tests for equals/hashCode in domain.
 */
class EntityEqualsHashTest {

    @Test
    void competenceEqualsHash() {
        Competence a = new Competence();
        Competence b = new Competence();
        assertThat(a).isNotEqualTo(b);

        a.setCompetenceId(1);
        b.setCompetenceId(1);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        b.setCompetenceId(2);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void personEqualsHash() {
        Person p1 = new Person();
        Person p2 = new Person();
        assertThat(p1).isNotEqualTo(p2);

        p1.setPersonId(10);
        p2.setPersonId(10);
        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void availabilityEqualsHash() {
        Availability av1 = new Availability();
        Availability av2 = new Availability();
        av1.setAvailabilityId(5);
        av2.setAvailabilityId(5);
        assertThat(av1).isEqualTo(av2);
    }
}
