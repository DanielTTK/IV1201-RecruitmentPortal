package se.kth.iv1201.recruitment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

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
        // ArgumentCaptor lets us catch the Person instance handed to personRepository.save()
        // so we can test fields populated on inside AccountService.
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
    } // lambda used above to write code directly into executable parameter

    @Test
    void whenEmailTakenThrows() {
        when(personRepository.existsByUsernameIgnoreCase(any())).thenReturn(false);
        when(personRepository.existsByPnr(any())).thenReturn(false);
        when(personRepository.existsByEmailIgnoreCase(any())).thenReturn(true);

        assertThrows(se.kth.iv1201.recruitment.application.error.EmailTakenException.class, () ->
            accountService.registerUser("A","S","alice","197101015678","a@a.com","secret"));
    }

    @Test
    void whenPersonNumberTakenThrows() {
        when(personRepository.existsByUsernameIgnoreCase(any())).thenReturn(false);
        when(personRepository.existsByPnr(any())).thenReturn(true);

        assertThrows(se.kth.iv1201.recruitment.application.error.PersonNumberTakenException.class, () ->
            accountService.registerUser("A","S","alice","197101015678","a@a.com","secret"));
    }

    @Test
    void whenPersonNumberInvalidThrows() {
        // Ensure username check passes so the method proceeds to pnr normalization/validation
        //when(personRepository.existsByUsernameIgnoreCase(any())).thenReturn(false);

        // Provide a person number with 9 digits "197101015" (Should be 12345678-1234. It is 12345678-9)
        // will not meet required 12 digits (8-4) and AccountService should throw IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () ->
            accountService.registerUser("A","S","alice","197101015","a@a.com","secret"));
    }

    @Test
    void completeLegacyUserUpdatesAndSaves() {
        Person legacy = new Person();
        legacy.setPersonId(5);
        legacy.setLegacy(true);
        legacy.setRoleId(1); // preserve

        // Optional.of(...) wraps the legacy Person in a non-empty Optional so
        // personRepository.findById(...) returns a present value (simulating a DB hit).
        when(personRepository.findById(5)).thenReturn(java.util.Optional.of(legacy));

        // when(...).thenAnswer(...) lets the mock return the same Person instance pased to save.
        // This simulates DB saved object, allowing us to check differences.
        when(personRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        accountService.completeLegacyUser(5, "First", "Last", "newuser", "19700101-7777", "u@u.com", "pw12345");

    // capture the Person passed to save() to inspect the changes made by the service
    ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
    verify(personRepository).save(captor.capture());
        Person saved = captor.getValue();

        assertThat(saved.getPersonId()).isEqualTo(5);
        assertThat(saved.getUsername()).isEqualTo("newuser");
        assertThat(saved.getPnr()).isEqualTo("19700101-7777");
        assertThat(encoder.matches("pw12345", saved.getPassword())).isTrue();
        assertThat(saved.isLegacy()).isFalse();
        assertThat(saved.getRoleId()).isEqualTo(1);
    }

    @Test
    void completeLegacyUserNotLegacyNoSave() {
        Person p = new Person();
        p.setPersonId(6);
        p.setLegacy(false);

        // If the repository returns a non-legacy person, the service should skip saving.
        when(personRepository.findById(6)).thenReturn(java.util.Optional.of(p));

        accountService.completeLegacyUser(6, "F","L","u","19700101-1111","e@e.com","pw");

        verify(personRepository, never()).save(any());
    }

    @Test
    void completeLegacyUserPersonNotFoundThrows() {
        // Simulate repository returning empty (no person with id 7)
        when(personRepository.findById(7)).thenReturn(java.util.Optional.empty());

        // expect an IllegalArgumentException when the person id doesn't exist.
        assertThrows(IllegalArgumentException.class, () ->
            accountService.completeLegacyUser(7, "F","L","u","19700101-1111","e@e.com","pw"));
    }
}