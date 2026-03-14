package se.kth.iv1201.recruitment.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class DomainEntitiesTest {

    @Test
    void personEqualsAndToString() {
        Person p1 = new Person();
        p1.setPersonId(1);
        p1.setName("Alice");
        p1.setSurname("Bob");

        Person p2 = new Person();
        p2.setPersonId(1);

        assertThat(p1).isEqualTo(p2);

        String s = p1.toString();
        assertThat(s).contains("personId=1").contains("Alice");
    }

    @Test
    void applicationEqualsAndStatus() {
        Application a = new Application();
        a.setApplicationId(10);
        a.setStatus(ApplicationStatus.UNHANDLED);

        Application b = new Application();
        b.setApplicationId(10);

        assertThat(a).isEqualTo(b);
        assertThat(a.getStatus()).isEqualTo(ApplicationStatus.UNHANDLED);
    }

    @Test
    void competenceEqualsAndName() {
        Competence c = new Competence();
        c.setCompetenceId(5);
        c.setName("Driving");

        Competence d = new Competence();
        d.setCompetenceId(5);

        assertThat(c).isEqualTo(d);
        assertThat(c.getName()).isEqualTo("Driving");
    }

    @Test
    void datesAndPerson() {
        Availability av = new Availability();
        Person p = new Person();
        p.setPersonId(7);
        av.setPerson(p);
        av.setFromDate(LocalDate.now().plus(1, java.time.temporal.ChronoUnit.DAYS));
        av.setToDate(LocalDate.now().plus(30, java.time.temporal.ChronoUnit.DAYS));

        assertThat(av.getPerson().getPersonId()).isEqualTo(7);
        assertThat(av.getFromDate()).isEqualTo(LocalDate.now().plus(1, java.time.temporal.ChronoUnit.DAYS));
        assertThat(av.getToDate()).isEqualTo(LocalDate.now().plus(30, java.time.temporal.ChronoUnit.DAYS));
    }

    @Test
    void competenceProfile_yearsAndLinks() {
        Person p = new Person(); 
        p.setPersonId(11);

        Competence comp = new Competence(); 
        comp.setCompetenceId(3);

        CompetenceProfile cp = new CompetenceProfile();
        cp.setCompetenceProfileId(99);
        cp.setYearsOfExperience(2);
        cp.setPerson(p);
        cp.setCompetence(comp);

        assertThat(cp.getYearsOfExperience()).isEqualTo(2);
        assertThat(cp.getPerson().getPersonId()).isEqualTo(11);
        assertThat(cp.getCompetence().getCompetenceId()).isEqualTo(3);
    }
}
