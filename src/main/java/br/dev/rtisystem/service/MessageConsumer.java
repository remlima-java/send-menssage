package br.dev.rtisystem.service;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@AllArgsConstructor
public class MessageConsumer {

    private final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    @KafkaListener(topics = "chat-group", groupId = "chat-group")
    public void listen(String message) {
        try {
            emitter.send(SseEmitter.event().name("message").data(message));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }

    public SseEmitter getEmitter() {
        return emitter;
    }
}
