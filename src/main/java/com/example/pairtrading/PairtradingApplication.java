package com.example.pairtrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class PairtradingApplication {
	public static void main(String[] args) {
		SpringApplication.run(PairtradingApplication.class, args);
	}

}
