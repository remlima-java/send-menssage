package br.dev.rtisystem.controller;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.model.dtos.UserDto;
import br.dev.rtisystem.service.interfaces.UserService;
import br.dev.rtisystem.service.queue.MessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private MessageProducer messageProducer;

    @InjectMocks
    private UserController userController;

    @Captor
    private ArgumentCaptor<UserDto> userDtoCaptor;

    private UserDto userDto;
    private UUID userId;
    private List<UserDto> userList;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        // Criar mensagens para o usuário
        List<MessageDto> messages = new ArrayList<>();
        MessageDto message = new MessageDto();
        message.setId(UUID.randomUUID());
        message.setEmitter("usuario_teste");
        message.setRecipient("destinatario");
        message.setContent("Mensagem de teste");
        messages.add(message);

        // Criar usuário de teste
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setUsername("usuario_teste");
        userDto.setMessages(messages);

        // Criar lista de usuários para testes
        userList = Arrays.asList(
                userDto,
                createUserDto("usuario2"),
                createUserDto("usuario3")
        );
    }

    private UserDto createUserDto(String username) {
        UserDto dto = new UserDto();
        dto.setId(UUID.randomUUID());
        dto.setUsername(username);
        dto.setMessages(new ArrayList<>());
        return dto;
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso e enviar para o produtor de mensagens")
    void saveShouldReturnSavedUserAndSendToProducer() {
        // Arrange
        when(userService.save(any(UserDto.class))).thenReturn(userDto);
        doNothing().when(messageProducer).send(anyString(), any(UserDto.class));

        // Act
        ResponseEntity<UserDto> response = userController.save(userDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());

        // Verificar se o serviço foi chamado e capturar o argumento
        verify(userService).save(userDtoCaptor.capture());

        // Verificar se todos os timestamps das mensagens foram atualizados
        UserDto capturedUserDto = userDtoCaptor.getValue();
        for(MessageDto message : capturedUserDto.getMessages()) {
            assertNotNull(message.getTimestamp());
            // Verificar se o timestamp foi definido recentemente (últimos 10 segundos)
            assertTrue(LocalDateTime.now().minusSeconds(10).isBefore(message.getTimestamp()));
        }

        // Verificar se o produtor de mensagens foi chamado com os parâmetros corretos
        verify(messageProducer).send(eq("chat-group"), eq(userDto));
    }

    @Test
    @DisplayName("Deve retornar todos os usuários")
    void getAllUsersShouldReturnListOfUsers() {
        // Arrange
        when(userService.findAll()).thenReturn(userList);

        // Act
        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        verify(userService).findAll();
    }

    @Test
    @DisplayName("Deve encontrar usuário por ID")
    void findByIdShouldReturnUser() {
        // Arrange
        when(userService.findById(userId)).thenReturn(userDto);

        // Act
        ResponseEntity<UserDto> response = userController.findById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
        verify(userService).findById(userId);
    }

    @Test
    @DisplayName("Deve excluir usuário por ID")
    void deleteByIdShouldReturnNoContent() {
        // Arrange
        doNothing().when(userService).delete(userId);

        // Act
        ResponseEntity<Void> response = userController.deleteById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).delete(userId);
    }
}