package matriphics.schedulerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "matriphics")

public class SchedulerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApiApplication.class, args);
	}

}
