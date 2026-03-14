package se.kth.iv1201.recruitment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import se.kth.iv1201.recruitment.domain.Person;

import java.util.Optional;

@org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
class PersonRepositoryJpaTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    void saveAndFindByUsernameIgnoreCase() {
        Person p = new Person();
        p.setName("Test");
        p.setSurname("User");
        p.setUsername("TestUser");
        p.setPnr("19900101-0001");
        p.setEmail("test@example.com");
        p.setPassword("OneTwo123");
        p.setRoleId(2);

        Person saved = personRepository.save(p);
        assertThat(saved.getPersonId()).isNotNull();

        Optional<Person> found = personRepository.findByUsernameIgnoreCase("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getPersonId()).isEqualTo(saved.getPersonId());
    }

    @Test
    void existsByEmailIgnoreCaseAndPnr() {
        Person p = new Person();
        p.setName("Another");
        p.setSurname("One");
        p.setUsername("another");
        p.setPnr("20000101-1234");
        p.setEmail("Another@Example.com");
        p.setPassword("OneTwo12");
        p.setRoleId(2);

        @SuppressWarnings("unused")
        Person saved = personRepository.save(p);

        assertThat(personRepository.existsByEmailIgnoreCase("another@example.com")).isTrue();
        assertThat(personRepository.existsByPnr("20000101-1234")).isTrue();
        assertThat(personRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase("no","another@example.com")).isTrue();
    }
}
