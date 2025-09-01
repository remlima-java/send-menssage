package br.dev.rtisystem.service.interfaces;

import br.dev.rtisystem.model.dtos.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto save(UserDto user);
    UserDto findById(UUID id);
    List<UserDto> findAll();
    void delete(UUID id); // void pq não retorna nada após excluir o usuário


}
