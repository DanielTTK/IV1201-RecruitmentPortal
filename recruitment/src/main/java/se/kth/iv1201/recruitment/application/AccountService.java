/**
 * Handles register/login-related logic.
 *
 * Here the “rules” are kept (unique username, password hashing etc) and log important events
 * (success/failure) without ever logging secrets. Remember PW hasing (task 7) and logging (task 9).
 *
 */


package se.kth.iv1201.recruitment.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.kth.iv1201.recruitment.domain.Person;
import se.kth.iv1201.recruitment.presentation.account.RegisterForm;
import se.kth.iv1201.recruitment.repository.PersonRepository;

@Service
public class AccountService {
    
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void registerUser(RegisterForm form){
        Person person = new Person();
        person.setName(form.getFirstName());
        person.setSurname(form.getLastName());
        person.setUsername(form.getUsername());
        person.setPnr(form.getPersonNumber());
        person.setEmail(form.getEmail());
        person.setPassword(passwordEncoder.encode(form.getPassword()));
        person.setRoleId(2);
        personRepository.save(person); // persists the user in the database
    }
}
