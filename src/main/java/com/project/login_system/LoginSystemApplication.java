package com.project.login_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class LoginSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoginSystemApplication.class, args);
	}
}