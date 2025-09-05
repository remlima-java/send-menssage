package br.dev.rtisystem.service.impl.auth;

import br.dev.rtisystem.model.dtos.login.AuthResponseDto;
import br.dev.rtisystem.model.dtos.login.LoginDto;
import br.dev.rtisystem.model.dtos.login.RegisterDto;
import br.dev.rtisystem.model.entity.User;
import br.dev.rtisystem.repository.UserRepository;
import br.dev.rtisystem.service.interfaces.auth.AuthService;
import br.dev.rtisystem.service.interfaces.auth.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto register(RegisterDto request) {
        // Verificar se o usuário já existe
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }

        // Criar um novo usuário
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .messages(new ArrayList<>())
                .build();

        // Salvar o usuário
        userRepository.save(user);

        // Gerar token JWT
        String jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        ));

        // Retornar a resposta
        return AuthResponseDto.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();

    }

    @Override
    public AuthResponseDto login(LoginDto request) {
        try{
        // Autenticar o usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Buscar o usuário
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Gerar token JWT
        String jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        ));

        // Retornar a resposta
        return AuthResponseDto.builder()
                .message("Login sucesso!")
                .token(jwtToken)
                .username(user.getUsername())
                .build();

        } catch (Exception e){
            return AuthResponseDto.builder()
                    .message("Login falhou")
                    .token(null)
                    .username(null)
                    .build();

        }



    }
}
