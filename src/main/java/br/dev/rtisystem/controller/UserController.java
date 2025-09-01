package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.dtos.UserDto;
import br.dev.rtisystem.service.interfaces.UserService;
import br.dev.rtisystem.service.queue.MessageProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    private final UserService service;
    private final MessageProducer producer;

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        log.info("Salvando: {}", userDto);
        userDto.getMessages().forEach(message -> message.setTimestamp(LocalDateTime.now()));
        UserDto save = this.service.save(userDto);
        producer.send("chat-group", save);
        return ResponseEntity.ok(save);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Iniciando mensagem getAllUsers");
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") UUID id) {
        log.info("Iniciando mensagem findById: {}", id);
        return ResponseEntity.ok(this.service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        log.info("Iniciando mensagem deleteById: {}", id);
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
