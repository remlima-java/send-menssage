package br.dev.rtisystem.service;

import br.dev.rtisystem.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User save(User user);
    User findById(UUID id);
    List<User> findAll();
    void delete(UUID id); // void pq não retorna nada após excluir o usuário


}
