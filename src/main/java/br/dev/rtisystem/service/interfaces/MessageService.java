package br.dev.rtisystem.service.interfaces;

import br.dev.rtisystem.model.dtos.MessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> getAllMessages();
}
