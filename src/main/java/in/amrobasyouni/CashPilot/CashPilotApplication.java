package in.amrobasyouni.CashPilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CashPilotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashPilotApplication.class, args);
	}

}
