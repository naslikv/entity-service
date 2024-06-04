package entityportal.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class EntityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntityServiceApplication.class, args);
	}

}
