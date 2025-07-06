package br.dev.rtisystem.model.dtos;

import br.dev.rtisystem.model.entity.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class UserDto {
    private UUID id;
    private String username;
    private List<Message> messages;
}
