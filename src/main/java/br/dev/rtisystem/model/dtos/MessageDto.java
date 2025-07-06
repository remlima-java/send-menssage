package br.dev.rtisystem.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto{

    private UUID id;
    private String from;
    private String to;
    private String content;
    private LocalDateTime timestamp;
    private String sender;
    private String receiver;
    private String username;


}
