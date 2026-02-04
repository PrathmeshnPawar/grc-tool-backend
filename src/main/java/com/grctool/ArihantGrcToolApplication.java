package com.grctool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArihantGrcToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArihantGrcToolApplication.class, args);
	}

}
