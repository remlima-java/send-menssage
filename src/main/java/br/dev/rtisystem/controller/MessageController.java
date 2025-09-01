package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.service.interfaces.MessageService;
import br.dev.rtisystem.service.interfaces.auth.JwtService;
import br.dev.rtisystem.service.queue.MessageConsumer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class MessageController {

    private final MessageService messageService;
    private final MessageConsumer messageConsumer;
    private final JwtService jwtService;

    @GetMapping("/stream")
    public ResponseEntity<SseEmitter> stream(@RequestParam(required = false) String token) {
        // Token é opcional para facilitar o desenvolvimento
        // Você pode torná-lo obrigatório em produção
        if (token != null) {
            try {
                String username = jwtService.extractUsername(token);
                if (username == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        return ResponseEntity.ok(this.messageConsumer.stream());
    }



    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        return ResponseEntity.ok(this.messageService.getAllMessages());
    }
}
