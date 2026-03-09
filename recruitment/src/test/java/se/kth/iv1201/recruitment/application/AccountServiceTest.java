package se.kth.iv1201.recruitment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import se.kth.iv1201.recruitment.application.error.UsernameTakenException;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private PersonRepository personRepository;

    private PasswordEncoder encoder;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder(4);
        accountService = new AccountService(personRepository, encoder);
    }

    @Test
    void encodePasswordAndSavePerson() {
        Person savedPerson = new Person();
        savedPerson.setPersonId(999);

        when(personRepository.existsByUsernameIgnoreCase(any())).thenReturn(false);
        when(personRepository.existsByPnr(any())).thenReturn(false);
        when(personRepository.existsByEmailIgnoreCase(any())).thenReturn(false);
        when(personRepository.save(any())).thenReturn(savedPerson);

        accountService.registerUser("Alice", "S", "alice", "197101015678", "a@a.com", "secret");

        // capture the Person object sent to save() and verify its attributes
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(captor.capture());
        Person saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("alice");
        assertThat(saved.getPnr()).isEqualTo("19710101-5678");
        assertThat(encoder.matches("secret", saved.getPassword())).isTrue();
    }

    @Test
    void whenUsernameTakenThrows() {
        when(personRepository.existsByUsernameIgnoreCase("alice")).thenReturn(true);
        assertThrows(UsernameTakenException.class, () ->
            accountService.registerUser("A","S","alice","197101015678","a@a.com","secret"));
    } // lambda used to write code directly into executable parameter
}