package br.dev.rtisystem.service.queue;

import br.dev.rtisystem.exceptions.JsonErrorException;
import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.model.dtos.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MessageConsumer messageConsumer;

    private UserDto userDto;
    private String jsonMessage;

    @BeforeEach
    void setUp() {
        // Criar usuário com mensagens
        userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setUsername("usuario_teste");

        List<MessageDto> messages = new ArrayList<>();
        MessageDto message = new MessageDto();
        message.setId(UUID.randomUUID());
        message.setEmitter("usuario_teste");
        message.setRecipient("destinatario");
        message.setContent("Mensagem de teste");
        messages.add(message);

        userDto.setMessages(messages);

        jsonMessage = "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"username\":\"usuario_teste\",\"messages\":[{\"id\":\"123e4567-e89b-12d3-a456-426614174001\",\"emitter\":\"usuario_teste\",\"recipient\":\"destinatario\",\"content\":\"Mensagem de teste\"}]}";
    }

    @Test
    @DisplayName("Deve criar e retornar um novo SseEmitter")
    void streamShouldCreateAndReturnNewEmitter() {
        // Act
        SseEmitter result = messageConsumer.stream();
        // Assert
        assertNotNull(result);
        // Nota: Não podemos testar facilmente se o emitter foi adicionado à lista privada
    }

    @Test
    @DisplayName("Deve processar mensagem JSON recebida corretamente")
    void listenShouldProcessJsonMessage() throws JsonProcessingException {
        // Arrange
        when(objectMapper.readValue(jsonMessage, UserDto.class)).thenReturn(userDto);

        // Act
        messageConsumer.listen(jsonMessage);

        // Assert
        verify(objectMapper).readValue(jsonMessage, UserDto.class);
    }

    @Test
    @DisplayName("Deve lançar JsonErrorException quando falhar ao converter JSON")
    void listenShouldThrowJsonErrorExceptionOnFailure() throws JsonProcessingException {
        // Arrange
        when(objectMapper.readValue(anyString(), eq(UserDto.class)))
                .thenThrow(new JsonProcessingException("Erro ao processar JSON") {});

        // Act & Assert
        assertThrows(JsonErrorException.class, () -> messageConsumer.listen(jsonMessage));
        verify(objectMapper).readValue(jsonMessage, UserDto.class);
    }

    @Test
    @DisplayName("Não deve enviar mensagem quando a lista de mensagens é nula")
    void listenShouldNotSendWhenMessagesAreNull() throws JsonProcessingException {
        // Arrange
        userDto.setMessages(null);
        when(objectMapper.readValue(jsonMessage, UserDto.class)).thenReturn(userDto);

        // Act
        messageConsumer.listen(jsonMessage);

        // Assert
        verify(objectMapper).readValue(jsonMessage, UserDto.class);
        // Não podemos verificar facilmente se nenhum emitter foi chamado devido à lista privada
    }

}