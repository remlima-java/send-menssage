package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.service.interfaces.MessageService;
import br.dev.rtisystem.service.interfaces.auth.JwtService;
import br.dev.rtisystem.service.queue.MessageConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private MessageConsumer messageConsumer;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private MessageController messageController;

    private SseEmitter mockEmitter;
    private List<MessageDto> mockMessages;
    private String validToken;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        mockEmitter = new SseEmitter();

        // Criando lista de mensagens mock
        mockMessages = Arrays.asList(
                createMessageDto("Usuário 1", "Usuário 2", "Olá, tudo bem?"),
                createMessageDto("Usuário 2", "Usuário 1", "Tudo ótimo, e você?")
        );

        validToken = "valid.jwt.token";
        invalidToken = "invalid.token";
    }

    private MessageDto createMessageDto(String emitter, String recipient, String content) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(UUID.randomUUID());
        messageDto.setEmitter(emitter);
        messageDto.setRecipient(recipient);
        messageDto.setContent(content);
        return messageDto;
    }

    @Test
    @DisplayName("Deve retornar SSE Emitter quando token é válido")
    void streamWithValidTokenShouldReturnSseEmitter() {
        // Arrange
        when(jwtService.extractUsername(validToken)).thenReturn("usuario_teste");
        when(messageConsumer.stream()).thenReturn(mockEmitter);

        // Act
        ResponseEntity<SseEmitter> response = messageController.stream(validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEmitter, response.getBody());
        verify(jwtService).extractUsername(validToken);
        verify(messageConsumer).stream();
    }

    @Test
    @DisplayName("Deve retornar UNAUTHORIZED quando token é inválido")
    void streamWithInvalidTokenShouldReturnUnauthorized() {
        // Arrange
        when(jwtService.extractUsername(invalidToken)).thenReturn(null);

        // Act
        ResponseEntity<SseEmitter> response = messageController.stream(invalidToken);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(jwtService).extractUsername(invalidToken);
        verifyNoInteractions(messageConsumer);
    }

    @Test
    @DisplayName("Deve retornar UNAUTHORIZED quando ocorre exceção na validação do token")
    void streamWithTokenExceptionShouldReturnUnauthorized() {
        // Arrange
        when(jwtService.extractUsername(invalidToken)).thenThrow(new RuntimeException("Token inválido"));

        // Act
        ResponseEntity<SseEmitter> response = messageController.stream(invalidToken);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(jwtService).extractUsername(invalidToken);
        verifyNoInteractions(messageConsumer);
    }

    @Test
    @DisplayName("Deve permitir stream sem token (para desenvolvimento)")
    void streamWithoutTokenShouldReturnSseEmitter() {
        // Arrange
        when(messageConsumer.stream()).thenReturn(mockEmitter);

        // Act
        ResponseEntity<SseEmitter> response = messageController.stream(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockEmitter, response.getBody());
        verifyNoInteractions(jwtService);
        verify(messageConsumer).stream();
    }

    @Test
    @DisplayName("Deve retornar todas as mensagens")
    void getAllMessagesShouldReturnListOfMessages() {
        // Arrange
        when(messageService.getAllMessages()).thenReturn(mockMessages);

        // Act
        ResponseEntity<List<MessageDto>> response = messageController.getAllMessages();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMessages, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(messageService).getAllMessages();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há mensagens")
    void getAllMessagesShouldReturnEmptyList() {
        // Arrange
        when(messageService.getAllMessages()).thenReturn(List.of());

        // Act
        ResponseEntity<List<MessageDto>> response = messageController.getAllMessages();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(messageService).getAllMessages();
    }
}