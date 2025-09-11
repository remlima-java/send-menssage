package br.dev.rtisystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class IsAlive {


    @GetMapping("isAlive")
    public ResponseEntity<String>isAlive() {
        log.warn("Só para teste" + IsAlive.class.getSimpleName());
        return ResponseEntity.ok("isAlive");
    }
}
