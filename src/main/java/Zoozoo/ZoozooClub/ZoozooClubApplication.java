package Zoozoo.ZoozooClub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ZoozooClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZoozooClubApplication.class, args);
	}

}
