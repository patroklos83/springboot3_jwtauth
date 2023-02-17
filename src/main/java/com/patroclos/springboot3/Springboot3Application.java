package com.patroclos.springboot3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.patroclos.*")
public class Springboot3Application {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/springboot3");
		SpringApplication.run(Springboot3Application.class, args);
	}
}
