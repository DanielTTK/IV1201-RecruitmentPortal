package se.kth.iv1201.recruitment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is the entry point of the Spring Boot application. It contains the main method which starts the application by 
 * calling SpringApplication.run().
 * 
 * @return the Spring Boot application
 */
@SpringBootApplication
public class RecruitmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitmentApplication.class, args);
	}

}
