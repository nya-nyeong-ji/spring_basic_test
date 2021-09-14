package com.example.spring_basic_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBasicTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBasicTestApplication.class, args);
	}

}
