package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.service.MessageService;
import br.dev.rtisystem.service.queue.MessageConsumer;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class MessageController {

    private final MessageService messageService;
    private final MessageConsumer messageConsumer;

    @GetMapping("/stream")
    public ResponseEntity<SseEmitter> stream() {
        return ResponseEntity.ok(this.messageConsumer.stream());
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        return ResponseEntity.ok(this.messageService.getAllMessages());
    }
}
