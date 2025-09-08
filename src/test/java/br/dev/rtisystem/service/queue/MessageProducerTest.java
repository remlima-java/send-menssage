package br.dev.rtisystem.service.queue;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.model.dtos.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageProducerTest {

    @Mock
    private KafkaTemplate<@NonNull String, @NonNull String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MessageProducer messageProducer;

    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<String> keyCaptor;

    @Captor
    private ArgumentCaptor<String> valueCaptor;

    private UserDto userDto;
    private String topic;
    private String jsonUser;

    @BeforeEach
    void setUp() {
        // Dados de teste
        topic = "chat-group";
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

        jsonUser = "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"username\":\"usuario_teste\",\"messages\":[{...}]}";
    }

    @Test
    @DisplayName("Deve enviar mensagem para o tópico Kafka com sucesso")
    void sendShouldSendMessageToKafkaTopic() throws JsonProcessingException {
        // Arrange
        when(objectMapper.writeValueAsString(userDto)).thenReturn(jsonUser);
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        // Act
        messageProducer.send(topic, userDto);

        // Assert
        verify(objectMapper).writeValueAsString(userDto);
        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), valueCaptor.capture());

        assertEquals(topic, topicCaptor.getValue());
        assertNotNull(keyCaptor.getValue()); // Usando o assertNotNull do JUnit
        assertEquals(jsonUser, valueCaptor.getValue());
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando falhar ao enviar para Kafka")
    void sendShouldThrowRuntimeExceptionOnFailure() throws JsonProcessingException {
        // Arrange
        when(objectMapper.writeValueAsString(userDto)).thenReturn(jsonUser);
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Kafka error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> messageProducer.send(topic, userDto));

        // Verificar a mensagem de erro
        assertEquals("Falha ao enviar mensagem para o tópico: ".concat(topic), exception.getMessage());

        verify(objectMapper).writeValueAsString(userDto);
        verify(kafkaTemplate).send(eq(topic), anyString(), eq(jsonUser));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando falhar ao serializar o objeto")
    void sendShouldThrowRuntimeExceptionOnSerializationFailure() throws JsonProcessingException {
        // Arrange
        when(objectMapper.writeValueAsString(userDto))
                .thenThrow(new JsonProcessingException("Serialization error") {});

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> messageProducer.send(topic, userDto));

        // Verificar a mensagem de erro
        assertEquals("Falha ao enviar mensagem para o tópico: ".concat(topic), exception.getMessage());

        verify(objectMapper).writeValueAsString(userDto);
        verifyNoInteractions(kafkaTemplate);
    }
}