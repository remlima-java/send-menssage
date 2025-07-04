package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.model.entity.Message;
import br.dev.rtisystem.service.MessageConsumer;
import br.dev.rtisystem.service.MessageProducer;
import br.dev.rtisystem.service.impl.MessageServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/message")
@Slf4j
public class MessageController {

    private final MessageServiceImpl service;

    private final MessageProducer producer;

    private final MessageConsumer consumer;

    @PostMapping
    public ResponseEntity<Message> message(@RequestBody Message message) {
        log.info("Iniciando mensagem: {}", message);
        this.producer.send("chat-group", message.toString());
        return ResponseEntity.ok(this.service.saveMessage(message));
    }

    @GetMapping("/stream")
    public SseEmitter stream() {
        return consumer.getEmitter();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> findById(@PathVariable("id") UUID id) {
        log.info("Iniciando mensagem findById: {}", id);
        return ResponseEntity.ok(this.service.getMessageById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteById(@PathVariable("id") UUID id) {
        log.info("Iniciando mensagem deleteById: {}", id);
        this.service.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

}
