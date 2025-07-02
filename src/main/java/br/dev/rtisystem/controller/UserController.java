package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.entity.User;
import br.dev.rtisystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        log.info("Salvando: {}", user);

        return ResponseEntity.ok(this.service.save(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Iniciando mensagem getAllUsers");
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") UUID id) {
        log.info("Iniciando mensagem findById: {}", id);
        return ResponseEntity.ok(this.service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteById(@PathVariable("id") UUID id) {
        log.info("Iniciando mensagem deleteById: {}", id);
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
