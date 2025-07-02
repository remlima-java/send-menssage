package br.dev.rtisystem.service;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.model.dtos.MessageDtozao;
import br.dev.rtisystem.model.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto sendMessage(MessageDto message);

    MessageDto getMessageById(UUID id);

    void deleteMessage(UUID id);

    List<MessageDtozao> getAllMessages();

    Message saveMessage(Message message);

}
