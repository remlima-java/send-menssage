package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.dtos.login.AuthResponseDto;
import br.dev.rtisystem.model.dtos.login.LoginDto;
import br.dev.rtisystem.model.dtos.login.RegisterDto;
import br.dev.rtisystem.service.interfaces.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}