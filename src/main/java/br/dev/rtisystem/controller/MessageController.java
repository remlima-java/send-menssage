package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.MessageDto;
import br.dev.rtisystem.service.impl.MessageServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/message")
@Slf4j
public class MessageController {

    private final MessageServiceImpl service;

    @PostMapping
    public ResponseEntity<MessageDto> message(@RequestBody MessageDto message) {
        log.info("Iniciando mensagem: {}", message);

        return ResponseEntity.ok(this.service.saveMessage(message));
    }

    @GetMapping
    public ResponseEntity<Set<MessageDto>> findAll() {
        log.info("Iniciando mensagem findAll");
        return ResponseEntity.ok(this.service.getAllMessages());
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
