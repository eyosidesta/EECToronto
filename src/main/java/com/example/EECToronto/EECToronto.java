package com.example.EECToronto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EECToronto {
	public static void main(String[] args) {
		SpringApplication.run(EECToronto.class, args);
	}
}