package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.exceptions.DeleteErrorException;
import br.dev.rtisystem.exceptions.UserNotFoundException;
import br.dev.rtisystem.model.dtos.UserDto;
import br.dev.rtisystem.model.entity.User;
import br.dev.rtisystem.repository.UserRepository;
import br.dev.rtisystem.service.interfaces.UserService;
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
    public UserDto save(UserDto userDto) {
        log.info("Saving user: {}", userDto);
        return this.modelMapper
                .map(this.userRepository
                        .save(this.modelMapper
                                .map(userDto, User.class)), UserDto.class);
    }

    @Override
    public UserDto findById(UUID id) {
        return this.userRepository
                .findById(id)
                    .map(user -> this.modelMapper.map(user, UserDto.class))
                        .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<UserDto> findAll() {
        return this.userRepository
                .findAll()
                    .stream()
                        .map(user -> this.modelMapper.map(user, UserDto.class)).toList();
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting user with id: {}", id);
        try {
            this.userRepository.delete(this.modelMapper.map(findById(id), User.class));
            log.info("User with id {} deleted successfully", id);
        } catch (DeleteErrorException e) {
            log.error("Error deleting user with id {}: {}", id, e.getMessage());
            throw new DeleteErrorException(e);
        }
    }


}
