package br.dev.rtisystem.model;

import br.dev.rtisystem.model.entity.Message;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MessageDto {

    private UUID id;
    private String from;
    private String to;
    private String content;

    private final LocalDateTime timestamp = LocalDateTime.now();

    public MessageDto(Message message) {
        this.id = message.getId();
        this.from = message.getFrom();
        this.to = message.getTo();
        this.content = message.getContent();
    }

    public MessageDto() {
    }
}
