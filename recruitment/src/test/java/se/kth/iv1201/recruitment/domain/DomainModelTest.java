package se.kth.iv1201.recruitment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DomainModelTest {

    @Test
    void personEqualsAndToString() {
        Person a = new Person();
        a.setPersonId(1);
        a.setUsername("u");
        a.setName("N");
        a.setSurname("E");

        Person b = new Person();
        b.setPersonId(1);

        Person c = new Person();
        c.setPersonId(2);

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("personId=1");
    }

    @Test
    void competenceEqualsAndToString() {
        Competence a = new Competence();
        a.setCompetenceId(10);
        a.setName("X");

        Competence b = new Competence();
        b.setCompetenceId(10);

        Competence c = new Competence();
        c.setCompetenceId(11);

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("competenceId=10");
    }

    @Test
    void applicationEqualsAndToString() {
        Application a = new Application();
        a.setApplicationId(100);

        Application b = new Application();
        b.setApplicationId(100);

        Application c = new Application();
        c.setApplicationId(101);

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("applicationId=100");
    }

    @Test
    void availabilityEqualsAndToString() {
        Availability a = new Availability();
        a.setAvailabilityId(20);

        Availability b = new Availability();
        b.setAvailabilityId(20);

        Availability c = new Availability();
        c.setAvailabilityId(21);

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("availabilityId=20");
    }

    @Test
    void competenceProfileEqualsAndToString() {
        CompetenceProfile a = new CompetenceProfile();
        a.setCompetenceProfileId(30);

        CompetenceProfile b = new CompetenceProfile();
        b.setCompetenceProfileId(30);

        CompetenceProfile c = new CompetenceProfile();
        c.setCompetenceProfileId(31);

        assertThat(a).isEqualTo(b);
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("competenceProfileId=30");
    }
}
