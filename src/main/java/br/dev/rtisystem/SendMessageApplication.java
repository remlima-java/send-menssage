package br.dev.rtisystem;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = {"br.dev.rtisystem.*"})
@EnableKafka
@EnableAdminServer
public class SendMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendMessageApplication.class, args);
	}

}
