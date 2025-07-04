package br.dev.rtisystem.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MessageProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        log.info("Enviando mensagem: {}", message);
        kafkaTemplate.send(topic, message);
    }
}
