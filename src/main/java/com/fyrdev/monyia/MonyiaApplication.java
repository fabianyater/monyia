package com.fyrdev.monyia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonyiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonyiaApplication.class, args);
	}

}
