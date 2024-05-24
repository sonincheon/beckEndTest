package com.tera.beckendtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeckEndTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(BeckEndTestApplication.class, args);
	}

}
