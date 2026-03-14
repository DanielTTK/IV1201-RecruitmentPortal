package se.kth.iv1201.recruitment.repository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.domain.Competence;

/**
 * Simply tests if the interfaces can be mocked and used as expected.
 */
@ExtendWith(MockitoExtension.class)
class RepositoryInterfacesTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    CompetenceRepository competenceRepository;

    @Test
    void personBasicMockBehaviour() {
        Person p = new Person();
        p.setPersonId(123);
        when(personRepository.findByUsernameIgnoreCase("u")).thenReturn(Optional.of(p));

        Optional<Person> r = personRepository.findByUsernameIgnoreCase("u");
        assertThat(r).isPresent();
        verify(personRepository).findByUsernameIgnoreCase("u");
    }

    @Test
    void competenceBasicMockBehaviour() {
        Competence c = new Competence();
        c.setCompetenceId(7);
        c.setName("X");
        when(competenceRepository.findByNameIgnoreCase("X")).thenReturn(Optional.of(c));

        Optional<Competence> r = competenceRepository.findByNameIgnoreCase("X");
        assertThat(r).isPresent();
        verify(competenceRepository).findByNameIgnoreCase("X");
    }
}
