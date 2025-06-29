package br.dev.rtisystem.service;

import br.dev.rtisystem.model.MessageDto;

import java.util.Set;
import java.util.UUID;

public interface MessageService {
    MessageDto sendMessage(MessageDto message);
    MessageDto getMessageById(UUID id);
    void deleteMessage(UUID id);
    Set<MessageDto> getAllMessages();
    MessageDto saveMessage(MessageDto message);

}
