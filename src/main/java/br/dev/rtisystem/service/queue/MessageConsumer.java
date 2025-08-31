package br.dev.rtisystem.service.queue;

import br.dev.rtisystem.exceptions.JsonErrorException;
import br.dev.rtisystem.exceptions.ListenErrorMessageException;
import br.dev.rtisystem.model.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@AllArgsConstructor
@Slf4j
public class MessageConsumer {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;

    public SseEmitter stream() {
        log.info("Criando novo emitter");
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitters.add(emitter);
        log.info("Emitter criado");
        return emitter;
    }

    @KafkaListener(topics = "chat-group", groupId = "chat-group-ui")
    public void listen(String message)  {
        log.info("Mensagem recebida: {}", message);

        User.UserBuilder builder;
        try {
            builder = objectMapper.readValue(message, User.UserBuilder.class);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter mensagem recebida para objeto User: {}", e.getMessage());
            throw new JsonErrorException(e);
        }

        User.UserBuilder finalBuilder = builder;
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("message").data(finalBuilder.build()));
                emitter.complete();
                log.info("Mensagem enviada para o cliente");
            } catch (IOException e) {
                log.error("Erro ao enviar mensagem para o cliente: {}", e.getMessage());
                throw new ListenErrorMessageException(e);
            }
        });

    }
}
