package br.dev.rtisystem.config.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();

    }
}
