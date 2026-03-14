package se.kth.iv1201.recruitment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.domain.Competence;

@org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
class RepositoryJpaTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CompetenceRepository competenceRepository;

    @Test
    void personSaveAndFindByUsername() {
        Person p = new Person();
        p.setName("Charlie");
        p.setSurname("Tester");
        p.setUsername("chuser");
        p.setPnr("19900101-0000");
        p.setEmail("ch@example.com");
        p.setPassword("OneTwo12");
        p.setRoleId(2);

        @SuppressWarnings("unused")
        Person saved = personRepository.save(p);

        assertThat(personRepository.findByUsernameIgnoreCase("chuser")).isPresent();
        assertThat(personRepository.existsByUsernameIgnoreCase("chuser")).isTrue();
        assertThat(personRepository.existsByEmailIgnoreCase("ch@example.com")).isTrue();
        assertThat(personRepository.existsByPnr("19900101-0000")).isTrue();
    }

    @Test
    void competenceSaveAndFindByName() {
        Competence c = new Competence();
        c.setName("CompetenceName");

        competenceRepository.save(c);

        assertThat(competenceRepository.findByNameIgnoreCase("CompetenceName")).isPresent();
        assertThat(competenceRepository.existsByNameIgnoreCase("CompetenceName")).isTrue();
    }
}
