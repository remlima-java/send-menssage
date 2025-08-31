package br.dev.rtisystem.model.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class MessageDto{

    private UUID id;
    private String emitter;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;


}
