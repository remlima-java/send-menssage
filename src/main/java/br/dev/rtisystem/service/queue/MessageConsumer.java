package br.dev.rtisystem.service.queue;

import br.dev.rtisystem.exceptions.JsonErrorException;
import br.dev.rtisystem.exceptions.ListenErrorMessageException;
import br.dev.rtisystem.model.dtos.UserDto;
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
        emitter.onCompletion(() -> {
            log.info("Emitter concluído, removendo da lista");
            emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            log.info("Emitter timeout, removendo da lista");
            emitters.remove(emitter);
        });
        emitters.add(emitter);
        log.info("Emitter criado e adicionado à lista. Total de emitters: {}", emitters.size());
        return emitter;
    }

    @KafkaListener(topics = "chat-group", groupId = "chat-group-ui")
    public void listen(String message) {
        log.info("Mensagem recebida do Kafka: {}", message);

        UserDto userDto;
        try {
            userDto = objectMapper.readValue(message, UserDto.class);
            log.info("Mensagem convertida com sucesso para objeto UserDto. Username: {}, Total de mensagens: {}",
                    userDto.getUsername(),
                    userDto.getMessages() != null ? userDto.getMessages().size() : 0);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter mensagem do Kafka para objeto UserDto: {}", e.getMessage(), e);
            throw new JsonErrorException(e);
        }

        if (userDto.getMessages() == null) {
            log.error("Erro ao ler mensagem do tópico: chat-group");
            return;
        }

        log.info("Enviando mensagens para {} emitters", emitters.size());

        emitters.forEach(emitter -> {
            try {
                log.debug("Enviando evento para um emitter");
                emitter.send(SseEmitter.event().name("message").data(userDto));
                log.info("Mensagem enviada com sucesso para o cliente");
            } catch (IOException e) {
                log.error("Erro ao enviar mensagem para o cliente: {}", e.getMessage(), e);
                emitters.remove(emitter);
                throw new ListenErrorMessageException(e);
            }
        });
    }
}