package com.rest.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TodoTaskServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoTaskServiceApplication.class, args);
	}

}
