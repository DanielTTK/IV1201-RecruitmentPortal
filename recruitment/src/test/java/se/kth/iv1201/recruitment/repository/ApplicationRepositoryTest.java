package se.kth.iv1201.recruitment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import se.kth.iv1201.recruitment.domain.Application;
import se.kth.iv1201.recruitment.domain.ApplicationStatus;
import se.kth.iv1201.recruitment.domain.Person;

@DataJpaTest
class ApplicationRepositoryTest {

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private PersonRepository personRepository;

	@Test
	void saveAndExistsByPersonPersonId() {
		Person p = new Person();
		p.setName("App");
		p.setSurname("Tester");
		p.setUsername("apptester");
		p.setPnr("19970101-0001");
		p.setEmail("app@example.com");
		p.setPassword("pw1234");
		p.setRoleId(2);

		Person saved = personRepository.save(p);

		Application app = new Application();
		app.setPerson(saved);
		app.setStatus(ApplicationStatus.UNHANDLED);

		applicationRepository.save(app);

		assertThat(applicationRepository.existsByPersonPersonId(saved.getPersonId())).isTrue();
		assertThat(applicationRepository.findAllByPersonPersonId(saved.getPersonId())).hasSize(1);
	}
}
