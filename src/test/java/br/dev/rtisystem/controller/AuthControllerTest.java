package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.dtos.login.AuthResponseDto;
import br.dev.rtisystem.model.dtos.login.LoginDto;
import br.dev.rtisystem.model.dtos.login.RegisterDto;
import br.dev.rtisystem.service.interfaces.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private RegisterDto registerDto;
    private LoginDto loginDto;
    private AuthResponseDto authResponseDto;

    @BeforeEach
    void setUp() {
        // Preparar dados para os testes
        registerDto = new RegisterDto("usuario_teste", "senha123");
        loginDto = new LoginDto("usuario_teste", "senha123");
        authResponseDto = AuthResponseDto.builder()
                .token("jwt-token-exemplo")
                .username("usuario_teste")
                .build();
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void registerShouldReturnAuthResponseDto() {
        // Arrange
        when(authService.register(any(RegisterDto.class))).thenReturn(authResponseDto);

        // Act
        ResponseEntity<AuthResponseDto> response = authController.register(registerDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponseDto, response.getBody());
        verify(authService, times(1)).register(registerDto);
    }

    @Test
    @DisplayName("Deve fazer login com sucesso")
    void loginShouldReturnAuthResponseDto() {
        // Arrange
        when(authService.login(any(LoginDto.class))).thenReturn(authResponseDto);

        // Act
        ResponseEntity<AuthResponseDto> response = authController.login(loginDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponseDto, response.getBody());
        verify(authService, times(1)).login(loginDto);
    }

    @Test
    @DisplayName("Deve verificar o comportamento quando o serviço retorna valores diferentes")
    void shouldHandleDifferentServiceResponses() {
        // Arrange
        AuthResponseDto diferenteResponse = AuthResponseDto.builder()
                .token("outro-token")
                .username("outro_usuario")
                .build();
        when(authService.register(any(RegisterDto.class))).thenReturn(diferenteResponse);

        // Act
        ResponseEntity<AuthResponseDto> response = authController.register(registerDto);

        // Assert
        assertEquals(diferenteResponse, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(diferenteResponse.getToken(), response.getBody().getToken());
        assertEquals(diferenteResponse.getUsername(), response.getBody().getUsername());
        verify(authService).register(any(RegisterDto.class));
    }

    @Test
    @DisplayName("Deve verificar a chamada correta do método do serviço no login")
    void loginShouldCallServiceMethodWithCorrectParams() {
        // Arrange
        LoginDto customLoginDto = new LoginDto("usuario_especifico", "senha_especifica");
        when(authService.login(customLoginDto)).thenReturn(authResponseDto);

        // Act
        ResponseEntity<AuthResponseDto> response = authController.login(customLoginDto);

        // Assert
        verify(authService).login(customLoginDto);
        verifyNoMoreInteractions(authService);
    }

    @Test
    @DisplayName("Deve verificar a chamada correta do método do serviço no registro")
    void registerShouldCallServiceMethodWithCorrectParams() {
        // Arrange
        RegisterDto customRegisterDto = new RegisterDto("novo_usuario", "nova_senha");
        when(authService.register(customRegisterDto)).thenReturn(authResponseDto);

        // Act
        ResponseEntity<AuthResponseDto> response = authController.register(customRegisterDto);

        // Assert
        verify(authService).register(customRegisterDto);
        verifyNoMoreInteractions(authService);
    }
}