package com.ftseoul.visitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VisitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(VisitorApplication.class, args);
	}

}
