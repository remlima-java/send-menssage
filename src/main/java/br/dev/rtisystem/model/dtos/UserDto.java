package br.dev.rtisystem.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private UUID id;
    private String username;
    private List<MessageDto> messages;
}
