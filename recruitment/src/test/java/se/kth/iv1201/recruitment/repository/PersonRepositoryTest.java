package se.kth.iv1201.recruitment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import se.kth.iv1201.recruitment.domain.Person;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void saveAndFindByUsername() {
        Person p = new Person();
        p.setUsername("bob");
        p.setName("Bob");
        p.setSurname("Builder");
        p.setEmail("bob@example.com");
        p.setPassword("encoded");

        personRepository.save(p);

        assertThat(personRepository.findByUsernameIgnoreCase("BOB")).isPresent();
        assertThat(personRepository.existsByEmailIgnoreCase("bob@example.com")).isTrue();
    }
}
