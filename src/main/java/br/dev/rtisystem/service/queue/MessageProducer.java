package br.dev.rtisystem.service.queue;

import br.dev.rtisystem.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private KafkaTemplate<String, String> kafkaTemplate;


    public void send(String topic, User user) {
        log.info("Enviando para tópico {}: {}", topic, user);
        try{
             kafkaTemplate.send(topic, new ObjectMapper().writeValueAsString(user));
        }catch (Exception e){
            log.error("Erro ao enviar mensagem para o tópico {}: {}", topic, e.getMessage());
            throw new RuntimeException("Falha ao enviar mensagem para o tópico: " + topic, e);
        }
    }
}
