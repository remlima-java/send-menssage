package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.model.entity.Message;
import br.dev.rtisystem.repository.MessageRepository;
import br.dev.rtisystem.service.MessageService;
import br.dev.rtisystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

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
        return this.messageRepository.findAll().stream().map(message -> {
            message.setSender(this.userService.getUser(UUID.fromString("15025033-f75e-4098-b15b-6b70b96de938")));
            return this.modelMapper.map(message, MessageDto.class);
        }).collect(Collectors.toSet());
    }

    @Override
    public Message saveMessage(Message message) {
        return this.messageRepository.save(message);
    }
}
