package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.exceptions.MessageNotFoundException;
import br.dev.rtisystem.model.MessageDto;
import br.dev.rtisystem.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final Set<MessageDto> messages = new LinkedHashSet<>();

    @Override
    public MessageDto sendMessage(MessageDto message) {
        return this.messages.add(message) ? message : null;
    }

    @Override
    public MessageDto getMessageById(UUID id) {
        return this.messages.stream().filter(m -> m.getId().equals(id)).findFirst()
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + id));
    }

    @Override
    public void deleteMessage(UUID id) {
        this.messages.remove(getMessageById(id));

    }

    @Override
    public Set<MessageDto> getAllMessages() {
        return this.messages;
    }

    @Override
    public MessageDto saveMessage(MessageDto message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        message.setId((UUID.randomUUID())); // Simple ID generation
        this.messages.add(message);
        return message;
    }


}
