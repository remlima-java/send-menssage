package br.dev.rtisystem.service;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.model.entity.Message;

import java.util.Set;
import java.util.UUID;

public interface MessageService {
    MessageDto sendMessage(MessageDto message);
    MessageDto getMessageById(UUID id);
    void deleteMessage(UUID id);
    Set<MessageDto> getAllMessages();
    Message saveMessage(Message message);

}
