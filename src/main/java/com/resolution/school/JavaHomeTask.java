package com.resolution.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JavaHomeTask {

	public static void main(String[] args) {
		SpringApplication.run(JavaHomeTask.class, args);
	}
}
