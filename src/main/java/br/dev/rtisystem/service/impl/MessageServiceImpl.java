package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.model.MessageDto;
import br.dev.rtisystem.model.entity.Message;
import br.dev.rtisystem.repository.MessageRepository;
import br.dev.rtisystem.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {


    private final MessageRepository messageRepository;


    @Override
    public MessageDto sendMessage(MessageDto message) {
        return null;
    }

    @Override
    public MessageDto getMessageById(UUID id) {
        return null;
    }

    @Override
    public void deleteMessage(UUID id) {

    }

    @Override
    public Set<MessageDto> getAllMessages() {
        return this.messageRepository.findAll()
                .stream()
                .map(MessageDto::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Message saveMessage(Message message) {
        return this.messageRepository.save(message);
    }
}
