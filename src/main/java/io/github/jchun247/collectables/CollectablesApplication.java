package io.github.jchun247.collectables;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CollectablesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectablesApplication.class, args);
	}

}
