package com.example.takehome;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TakehomeApplication {



	public static void main(String[] args) {
		log.debug("This is a debug message");
		SpringApplication.run(TakehomeApplication.class, args);

	}

}
