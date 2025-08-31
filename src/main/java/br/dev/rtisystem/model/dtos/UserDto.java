package br.dev.rtisystem.model.dtos;

import br.dev.rtisystem.model.entity.Message;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private UUID id;
    private String username;
    private List<Message> messages;
}
