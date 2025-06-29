package br.dev.rtisystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"br.dev.rtisystem.*"})
public class SendMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendMessageApplication.class, args);
	}

}
