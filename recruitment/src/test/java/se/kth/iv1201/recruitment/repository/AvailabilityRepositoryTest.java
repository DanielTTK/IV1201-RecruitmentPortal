package se.kth.iv1201.recruitment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import se.kth.iv1201.recruitment.domain.Availability;
import se.kth.iv1201.recruitment.domain.Person;

@DataJpaTest
class AvailabilityRepositoryTest {

	@Autowired
	private AvailabilityRepository availabilityRepository;

	@Autowired
	private PersonRepository personRepository;

	@Test
	void saveAndFindByPersonPersonId() {
		Person p = new Person();
		p.setName("Avail");
		p.setSurname("Tester");
		p.setUsername("apptester");
		p.setPnr("19970101-0001");
		p.setEmail("app@example.com");
		p.setPassword("pw1234");
		p.setRoleId(2);
        

		Person saved = personRepository.save(p);

		Availability a = new Availability();
		a.setPerson(saved);
		a.setFromDate(LocalDate.now().plusDays(1));
		a.setToDate(LocalDate.now().plusDays(10));

		availabilityRepository.save(a);

		assertThat(availabilityRepository.findAllByPersonPersonId(saved.getPersonId())).hasSize(1);
		assertThat(availabilityRepository.existsByPersonPersonId(saved.getPersonId())).isTrue();

		// clean up
		availabilityRepository.deleteAllByPersonPersonId(saved.getPersonId());
		assertThat(availabilityRepository.existsByPersonPersonId(saved.getPersonId())).isFalse();
	}
}
