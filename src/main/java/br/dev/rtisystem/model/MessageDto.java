package br.dev.rtisystem.model;

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

}
