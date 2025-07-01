package br.dev.rtisystem.service;

import br.dev.rtisystem.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User save(User user);
    User getUser(UUID id);
    List<User> getAllUsers();


}
