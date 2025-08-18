package br.dev.rti.darkstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DarkStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DarkStoreApplication.class, args);
    }

}
