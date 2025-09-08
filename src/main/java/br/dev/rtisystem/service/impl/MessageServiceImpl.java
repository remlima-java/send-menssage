package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.repository.MessageRepository;
import br.dev.rtisystem.service.interfaces.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<MessageDto> getAllMessages() {
        log.info("Fetching all messages");
        return this.messageRepository.findAll().stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .toList().reversed();
    }

}
