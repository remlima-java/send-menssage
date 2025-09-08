package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.repository.MessageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MessageServiceImpl messageService;


    @Test
    @DisplayName("Deve retornar lista vazia quando não há mensagens")
    void getAllMessagesShouldReturnEmptyList() {
        // Arrange
        when(messageRepository.findAll()).thenReturn(List.of());

        // Act
        List<MessageDto> result = messageService.getAllMessages();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(messageRepository).findAll();
        verifyNoMoreInteractions(modelMapper);
    }


}