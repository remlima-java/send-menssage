package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.exceptions.UserNotFoundException;
import br.dev.rtisystem.model.entity.User;
import br.dev.rtisystem.repository.UserRepository;
import br.dev.rtisystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User save(User user) {
        log.info("Saving user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public User getUser(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }


}
