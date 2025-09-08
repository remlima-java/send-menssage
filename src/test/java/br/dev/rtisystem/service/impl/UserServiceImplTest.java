package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.exceptions.DeleteErrorException;
import br.dev.rtisystem.exceptions.UserNotFoundException;
import br.dev.rtisystem.model.dtos.MessageDto;
import br.dev.rtisystem.model.dtos.UserDto;
import br.dev.rtisystem.model.entity.Message;
import br.dev.rtisystem.model.entity.User;
import br.dev.rtisystem.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserServiceImpl.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockitoBean
    private ModelMapper modelMapper;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Test {@link UserServiceImpl#save(UserDto)} $ $ then return Id is randomUUID.
     *
     * <p>Method under test: {@link UserServiceImpl#save(UserDto)}
     */
    @Test
    @DisplayName("Test save(UserDto) $ $ then return Id is randomUUID")
    @Tag("MaintainedByDiffblue")
    void testSavethenReturnIdIsRandomUUID() {
        // Arrange
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.any())).thenReturn(user);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");

        // Act
        UserDto actualSaveResult = userServiceImpl.save(userDto);

        // Assert
        verify(userRepository).save(isA(User.class));
        assertEquals("janedoe", actualSaveResult.getUsername());
        assertTrue(actualSaveResult.getMessages().isEmpty());
        assertSame(id, actualSaveResult.getId());
    }

    /**
     * Test {@link UserServiceImpl#save(UserDto)} $ given {@link Message#Message()} Content is {@code
     * Not all who wander are lost} $ then return Messages size is one.
     *
     * <p>Method under test: {@link UserServiceImpl#save(UserDto)}
     */
    @Test
    @DisplayName(
            "Test save(UserDto) $ given Message() Content is 'Not all who wander are lost' $ then return Messages size is one")
    @Tag("MaintainedByDiffblue")
    void testSave$givenMessageContentIsNotAllWhoWanderAreLost$thenReturnMessagesSizeIsOne() {
        // Arrange
        Message message = new Message();
        message.setContent("Not all who wander are lost");
        message.setEmitter("Saving user: {}");
        message.setId(UUID.randomUUID());
        message.setRecipient("Saving user: {}");
        message.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(messages);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.any())).thenReturn(user);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");

        // Act
        UserDto actualSaveResult = userServiceImpl.save(userDto);

        // Assert
        verify(userRepository).save(isA(User.class));
        List<MessageDto> messages2 = actualSaveResult.getMessages();
        assertEquals(1, messages2.size());
        MessageDto getResult = messages2.get(0);
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Saving user: {}", getResult.getEmitter());
        assertEquals("Saving user: {}", getResult.getRecipient());
    }

    /**
     * Test {@link UserServiceImpl#save(UserDto)} $ given {@link Message#Message()} Content is {@code
     * Saving user: {}} $ then return Messages size is two.
     *
     * <p>Method under test: {@link UserServiceImpl#save(UserDto)}
     */
    @Test
    @DisplayName(
            "Test save(UserDto) $ given Message() Content is 'Saving user: {}' $ then return Messages size is two")
    @Tag("MaintainedByDiffblue")
    void testSave$givenMessageContentIsSavingUser$thenReturnMessagesSizeIsTwo() {
        // Arrange
        Message message = new Message();
        message.setContent("Not all who wander are lost");
        message.setEmitter("Saving user: {}");
        UUID id = UUID.randomUUID();
        message.setId(id);
        message.setRecipient("Saving user: {}");
        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        message.setTimestamp(ofResult.atStartOfDay());

        Message message2 = new Message();
        message2.setContent("Saving user: {}");
        message2.setEmitter("Emitter");
        message2.setId(UUID.randomUUID());
        message2.setRecipient("Recipient");
        message2.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message2);
        messages.add(message);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(messages);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.any())).thenReturn(user);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");

        // Act
        UserDto actualSaveResult = userServiceImpl.save(userDto);

        // Assert
        verify(userRepository).save(isA(User.class));
        List<MessageDto> messages2 = actualSaveResult.getMessages();
        assertEquals(2, messages2.size());
        MessageDto getResult = messages2.get(1);
        LocalDate toLocalDateResult = getResult.getTimestamp().toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        MessageDto getResult2 = messages2.get(0);
        assertEquals("Emitter", getResult2.getEmitter());
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Recipient", getResult2.getRecipient());
        assertEquals("Saving user: {}", getResult2.getContent());
        assertEquals("Saving user: {}", getResult.getEmitter());
        assertEquals("Saving user: {}", getResult.getRecipient());
        assertSame(ofResult, toLocalDateResult);
        assertSame(id, getResult.getId());
    }

    /**
     * Test {@link UserServiceImpl#save(UserDto)} $ given {@link ModelMapper} {@link
     * ModelMapper#map(Object, Class)} return {@code null} $ then return {@code null}.
     *
     * <p>Method under test: {@link UserServiceImpl#save(UserDto)}
     */
    @Test
    @DisplayName(
            "Test save(UserDto) $ given ModelMapper map(Object, Class) return 'null' $ then return 'null'")
    @Tag("MaintainedByDiffblue")
    void testSave$givenModelMapperMapReturnNull$thenReturnNull() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        when(userRepository.save(Mockito.any())).thenReturn(user);
        when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(null);

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");

        // Act
        UserDto actualSaveResult = userServiceImpl.save(userDto);

        // Assert
        verify(modelMapper, atLeast(1)).map(Mockito.any(), Mockito.any());
        verify(userRepository).save(isNull());
        assertNull(actualSaveResult);
    }

    /**
     * Test {@link UserServiceImpl#save(UserDto)} $ given {@link User#User()} Id is {@code null} $
     * then return Id is {@code null}.
     *
     * <p>Method under test: {@link UserServiceImpl#save(UserDto)}
     */
    @Test
    @DisplayName("Test save(UserDto) $ given User() Id is 'null' $ then return Id is 'null'")
    @Tag("MaintainedByDiffblue")
    void testSave$givenUserIdIsNull$thenReturnIdIsNull() {
        // Arrange
        User user = new User();
        user.setId(null);
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.save(Mockito.any())).thenReturn(user);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");

        // Act
        UserDto actualSaveResult = userServiceImpl.save(userDto);

        // Assert
        verify(userRepository).save(isA(User.class));
        assertEquals("janedoe", actualSaveResult.getUsername());
        assertNull(actualSaveResult.getId());
        assertTrue(actualSaveResult.getMessages().isEmpty());
    }

    /**
     * Test {@link UserServiceImpl#save(UserDto)} $ given {@link UserRepository} $ then throw {@link
     * DeleteErrorException}.
     *
     * <p>Method under test: {@link UserServiceImpl#save(UserDto)}
     */
    @Test
    @DisplayName("Test save(UserDto) $ given UserRepository $ then throw DeleteErrorException")
    @Tag("MaintainedByDiffblue")
    void testSave$givenUserRepository$thenThrowDeleteErrorException() {
        // Arrange
        when(modelMapper.map(Mockito.any(), Mockito.any()))
                .thenThrow(new DeleteErrorException(new Exception()));

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.save(userDto));
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
    }

    /**
     * Test {@link UserServiceImpl#save(UserDto)} $ given {@link UserRepository} {@link
     * UserRepository#save(Object)} throw {@link DeleteErrorException#DeleteErrorException(Exception)}
     * with message is {@link Exception#Exception()} $.
     *
     * <p>Method under test: {@link UserServiceImpl#save(UserDto)}
     */
    @Test
    @DisplayName(
            "Test save(UserDto) $ given UserRepository save(Object) throw DeleteErrorException(Exception) with message is Exception() $")
    @Tag("MaintainedByDiffblue")
    void testSave$givenUserRepositorySaveThrowDeleteErrorExceptionWithMessageIsException$() {
        // Arrange
        when(userRepository.save(Mockito.any()))
                .thenThrow(new DeleteErrorException(new Exception()));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        when(modelMapper.map(Mockito.any(), Mockito.<Class<User>>any())).thenReturn(user);

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.save(userDto));
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).save(isA(User.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ $.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName("Test delete(UUID) $ $")
    void testDelete() {
        // Arrange
        when(userRepository.findById(Mockito.any()))
                .thenThrow(new DeleteErrorException(new Exception()));

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.delete(UUID.randomUUID()));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ $.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName("Test delete(UUID) $ $")
    void testDelete2() {
        // Arrange
        when(userRepository.findById(Mockito.any()))
                .thenThrow(new UserNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.delete(UUID.randomUUID()));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ $.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName("Test delete(UUID) $ $")
    void testDelete3() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        doThrow(new DeleteErrorException(new Exception()))
                .when(userRepository)
                .delete(Mockito.any());
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.delete(UUID.randomUUID()));
        verify(userRepository).delete(isA(User.class));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ $.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName("Test delete(UUID) $ $")
    @Tag("MaintainedByDiffblue")
    void testDelete4() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        doThrow(new UserNotFoundException("An error occurred"))
                .when(userRepository)
                .delete(Mockito.any());
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.delete(UUID.randomUUID()));
        verify(userRepository).delete(isA(User.class));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ given {@link Message#Message()} Content is {@code
     * Deleting user with id: {}} $ then calls {@link UserRepository#delete(Object)}.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName(
            "Test delete(UUID) $ given Message() Content is 'Deleting user with id: {}' $ then calls delete(Object)")
    @Tag("MaintainedByDiffblue")
    void testDelete$givenMessageContentIsDeletingUserWithId$thenCallsDelete() {
        // Arrange
        Message message = new Message();
        message.setContent("Not all who wander are lost");
        message.setEmitter("Deleting user with id: {}");
        message.setId(UUID.randomUUID());
        message.setRecipient("Deleting user with id: {}");
        message.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        Message message2 = new Message();
        message2.setContent("Deleting user with id: {}");
        message2.setEmitter("User with id {} deleted successfully");
        message2.setId(UUID.randomUUID());
        message2.setRecipient("User with id {} deleted successfully");
        message2.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message2);
        messages.add(message);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(messages);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        doNothing().when(userRepository).delete(Mockito.any());
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act
        userServiceImpl.delete(UUID.randomUUID());

        // Assert
        verify(userRepository).delete(isA(User.class));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ given {@link Message#Message()} Content is {@code
     * Not all who wander are lost} $ then calls {@link UserRepository#delete(Object)}.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName(
            "Test delete(UUID) $ given Message() Content is 'Not all who wander are lost' $ then calls delete(Object)")
    @Tag("MaintainedByDiffblue")
    void testDelete$givenMessageContentIsNotAllWhoWanderAreLost$thenCallsDelete() {
        // Arrange
        Message message = new Message();
        message.setContent("Not all who wander are lost");
        message.setEmitter("Deleting user with id: {}");
        message.setId(UUID.randomUUID());
        message.setRecipient("Deleting user with id: {}");
        message.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(messages);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        doNothing().when(userRepository).delete(Mockito.any());
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act
        userServiceImpl.delete(UUID.randomUUID());

        // Assert
        verify(userRepository).delete(isA(User.class));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ given {@link ModelMapper} {@link
     * ModelMapper#map(Object, Class)} throw {@link
     * DeleteErrorException#DeleteErrorException(Exception)} with message is {@link
     * Exception#Exception()} $.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName(
            "Test delete(UUID) $ given ModelMapper map(Object, Class) throw DeleteErrorException(Exception) with message is Exception() $")
    void testDelete$givenModelMapperMapThrowDeleteErrorExceptionWithMessageIsException$() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        when(modelMapper.map(Mockito.any(), Mockito.<Class<UserDto>>any()))
                .thenThrow(new DeleteErrorException(new Exception()));

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.delete(UUID.randomUUID()));
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ given {@link User#User()} Id is {@code null} $ then
     * calls {@link UserRepository#delete(Object)}.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName("Test delete(UUID) $ given User() Id is 'null' $ then calls delete(Object)")
    @Tag("MaintainedByDiffblue")
    void testDelete$givenUserIdIsNull$thenCallsDelete() {
        // Arrange
        User user = new User();
        user.setId(null);
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        doNothing().when(userRepository).delete(Mockito.any());
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act
        userServiceImpl.delete(UUID.randomUUID());

        // Assert
        verify(userRepository).delete(isA(User.class));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#delete(UUID)} $ given {@link UserRepository} {@link
     * UserRepository#delete(Object)} does nothing $ then calls {@link UserRepository#delete(Object)}.
     *
     * <p>Method under test: {@link UserServiceImpl#delete(UUID)}
     */
    @Test
    @DisplayName(
            "Test delete(UUID) $ given UserRepository delete(Object) does nothing $ then calls delete(Object)")
    void testDelete$givenUserRepositoryDeleteDoesNothing$thenCallsDelete() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        doNothing().when(userRepository).delete(Mockito.any());
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act
        userServiceImpl.delete(UUID.randomUUID());

        // Assert
        verify(userRepository).delete(isA(User.class));
        verify(userRepository).findById(isA(UUID.class));
    }

    

    /**
     * Test {@link UserServiceImpl#findById(UUID)} $ $.
     *
     * <p>Method under test: {@link UserServiceImpl#findById(UUID)}
     */
    @Test
    @DisplayName("Test findById(UUID) $ $")
    @Tag("MaintainedByDiffblue")
    void testFindById() {
        // Arrange
        when(userRepository.findById(Mockito.any()))
                .thenThrow(new DeleteErrorException(new Exception()));

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.findById(UUID.randomUUID()));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#findById(UUID)} $ $ then return Id is randomUUID.
     *
     * <p>Method under test: {@link UserServiceImpl#findById(UUID)}
     */
    @Test
    @DisplayName("Test findById(UUID) $ $ then return Id is randomUUID")
    @Tag("MaintainedByDiffblue")
    void testFindByIdthenReturnIdIsRandomUUID() {
        // Arrange
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act
        UserDto actualFindByIdResult = userServiceImpl.findById(UUID.randomUUID());

        // Assert
        verify(userRepository).findById(isA(UUID.class));
        assertEquals("janedoe", actualFindByIdResult.getUsername());
        assertTrue(actualFindByIdResult.getMessages().isEmpty());
        assertSame(id, actualFindByIdResult.getId());
    }

    /**
     * Test {@link UserServiceImpl#findById(UUID)} $ $ then return Messages size is one.
     *
     * <p>Method under test: {@link UserServiceImpl#findById(UUID)}
     */
    @Test
    @DisplayName("Test findById(UUID) $ $ then return Messages size is one")
    @Tag("MaintainedByDiffblue")
    void testFindByIdthenReturnMessagesSizeIsOne() {
        // Arrange
        Message message = new Message();
        message.setContent("Not all who wander are lost");
        message.setEmitter("Emitter");
        message.setId(UUID.randomUUID());
        message.setRecipient("Recipient");
        message.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(messages);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act
        UserDto actualFindByIdResult = userServiceImpl.findById(UUID.randomUUID());

        // Assert
        verify(userRepository).findById(isA(UUID.class));
        List<MessageDto> messages2 = actualFindByIdResult.getMessages();
        assertEquals(1, messages2.size());
        MessageDto getResult = messages2.get(0);
        assertEquals("Emitter", getResult.getEmitter());
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Recipient", getResult.getRecipient());
    }

  
    /**
     * Test {@link UserServiceImpl#findById(UUID)} $ given {@link Message#Message()} Content is {@code
     * Content} $ then return Messages size is two.
     *
     * <p>Method under test: {@link UserServiceImpl#findById(UUID)}
     */
    @Test
    @DisplayName(
            "Test findById(UUID) $ given Message() Content is 'Content' $ then return Messages size is two")
    @Tag("MaintainedByDiffblue")
    void testFindById$givenMessageContentIsContent$thenReturnMessagesSizeIsTwo() {
        // Arrange
        Message message = new Message();
        message.setContent("Not all who wander are lost");
        message.setEmitter("Emitter");
        UUID id = UUID.randomUUID();
        message.setId(id);
        message.setRecipient("Recipient");
        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        message.setTimestamp(ofResult.atStartOfDay());

        Message message2 = new Message();
        message2.setContent("Content");
        message2.setEmitter("42");
        message2.setId(UUID.randomUUID());
        message2.setRecipient("42");
        message2.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message2);
        messages.add(message);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(messages);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult2);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act
        UserDto actualFindByIdResult = userServiceImpl.findById(UUID.randomUUID());

        // Assert
        verify(userRepository).findById(isA(UUID.class));
        List<MessageDto> messages2 = actualFindByIdResult.getMessages();
        assertEquals(2, messages2.size());
        MessageDto getResult = messages2.get(1);
        LocalDate toLocalDateResult = getResult.getTimestamp().toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        MessageDto getResult2 = messages2.get(0);
        assertEquals("42", getResult2.getEmitter());
        assertEquals("42", getResult2.getRecipient());
        assertEquals("Content", getResult2.getContent());
        assertEquals("Emitter", getResult.getEmitter());
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Recipient", getResult.getRecipient());
        assertSame(ofResult, toLocalDateResult);
        assertSame(id, getResult.getId());
    }

    /**
     * Test {@link UserServiceImpl#findById(UUID)} $ given {@link ModelMapper} {@link
     * ModelMapper#map(Object, Class)} throw {@link
     * DeleteErrorException#DeleteErrorException(Exception)} with message is {@link
     * Exception#Exception()} $.
     *
     * <p>Method under test: {@link UserServiceImpl#findById(UUID)}
     */
    @Test
    @DisplayName(
            "Test findById(UUID) $ given ModelMapper map(Object, Class) throw DeleteErrorException(Exception) with message is Exception() $")
    @Tag("MaintainedByDiffblue")
    void testFindById$givenModelMapperMapThrowDeleteErrorExceptionWithMessageIsException$() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        when(modelMapper.map(Mockito.any(), Mockito.<Class<UserDto>>any()))
                .thenThrow(new DeleteErrorException(new Exception()));

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.findById(UUID.randomUUID()));
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link UserServiceImpl#findById(UUID)} $ given {@link UserDto} (default constructor) Id is
     * randomUUID $ then return {@link UserDto} (default constructor).
     *
     * <p>Method under test: {@link UserServiceImpl#findById(UUID)}
     */
    @Test
    @DisplayName(
            "Test findById(UUID) $ given UserDto (default constructor) Id is randomUUID $ then return UserDto (default constructor)")
    @Tag("MaintainedByDiffblue")
    void testFindById$givenUserDtoIdIsRandomUUID$thenReturnUserDto() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");
        when(modelMapper.map(Mockito.any(), Mockito.<Class<UserDto>>any())).thenReturn(userDto);

        // Act
        UserDto actualFindByIdResult = userServiceImpl.findById(UUID.randomUUID());

        // Assert
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findById(isA(UUID.class));
        assertSame(userDto, actualFindByIdResult);
    }

    /**
     * Test {@link UserServiceImpl#findById(UUID)} $ given {@link User#User()} Id is {@code null} $
     * then return Id is {@code null}.
     *
     * <p>Method under test: {@link UserServiceImpl#findById(UUID)}
     */
    @Test
    @DisplayName("Test findById(UUID) $ given User() Id is 'null' $ then return Id is 'null'")
    @Tag("MaintainedByDiffblue")
    void testFindById$givenUserIdIsNull$thenReturnIdIsNull() {
        // Arrange
        User user = new User();
        user.setId(null);
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findById(Mockito.any())).thenReturn(ofResult);
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, new ModelMapper());

        // Act
        UserDto actualFindByIdResult = userServiceImpl.findById(UUID.randomUUID());

        // Assert
        verify(userRepository).findById(isA(UUID.class));
        assertEquals("janedoe", actualFindByIdResult.getUsername());
        assertNull(actualFindByIdResult.getId());
        assertTrue(actualFindByIdResult.getMessages().isEmpty());
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ $.
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName("Test findAll() $ $")
    @Tag("MaintainedByDiffblue")
    void testFindAll() {
        // Arrange
        when(userRepository.findAll()).thenThrow(new DeleteErrorException(new Exception()));

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.findAll());
        verify(userRepository).findAll();
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ $ then return first Id is randomUUID.
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName("Test findAll() $ $ then return first Id is randomUUID")
    @Tag("MaintainedByDiffblue")
    void testFindAllthenReturnFirstIdIsRandomUUID() {
        // Arrange
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDto> actualFindAllResult =
                new UserServiceImpl(userRepository, new ModelMapper()).findAll();

        // Assert
        verify(userRepository).findAll();
        assertEquals(1, actualFindAllResult.size());
        UserDto getResult = actualFindAllResult.get(0);
        assertEquals("janedoe", getResult.getUsername());
        assertTrue(getResult.getMessages().isEmpty());
        assertSame(id, getResult.getId());
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ $ then return first Messages size is one.
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName("Test findAll() $ $ then return first Messages size is one")
    @Tag("MaintainedByDiffblue")
    void testFindAllthenReturnFirstMessagesSizeIsOne() {
        // Arrange
        Message message = new Message();
        message.setContent("Not all who wander are lost");
        message.setEmitter("Emitter");
        message.setId(UUID.randomUUID());
        message.setRecipient("Recipient");
        message.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(messages);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDto> actualFindAllResult =
                new UserServiceImpl(userRepository, new ModelMapper()).findAll();

        // Assert
        verify(userRepository).findAll();
        assertEquals(1, actualFindAllResult.size());
        List<MessageDto> messages2 = actualFindAllResult.get(0).getMessages();
        assertEquals(1, messages2.size());
        MessageDto getResult = messages2.get(0);
        assertEquals("Emitter", getResult.getEmitter());
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Recipient", getResult.getRecipient());
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ given {@link Message#Message()} Content is {@code
     * Content} $ then return first Messages size is two.
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName(
            "Test findAll() $ given Message() Content is 'Content' $ then return first Messages size is two")
    @Tag("MaintainedByDiffblue")
    void testFindAll$givenMessageContentIsContent$thenReturnFirstMessagesSizeIsTwo() {
        // Arrange
        Message message = new Message();
        message.setContent("Not all who wander are lost");
        message.setEmitter("Emitter");
        UUID id = UUID.randomUUID();
        message.setId(id);
        message.setRecipient("Recipient");
        LocalDate ofResult = LocalDate.of(1970, 1, 1);
        message.setTimestamp(ofResult.atStartOfDay());

        Message message2 = new Message();
        message2.setContent("Content");
        message2.setEmitter("42");
        message2.setId(UUID.randomUUID());
        message2.setRecipient("42");
        message2.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message2);
        messages.add(message);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(messages);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDto> actualFindAllResult =
                new UserServiceImpl(userRepository, new ModelMapper()).findAll();

        // Assert
        verify(userRepository).findAll();
        assertEquals(1, actualFindAllResult.size());
        List<MessageDto> messages2 = actualFindAllResult.get(0).getMessages();
        assertEquals(2, messages2.size());
        MessageDto getResult = messages2.get(1);
        LocalDate toLocalDateResult = getResult.getTimestamp().toLocalDate();
        assertEquals("1970-01-01", toLocalDateResult.toString());
        MessageDto getResult2 = messages2.get(0);
        assertEquals("42", getResult2.getEmitter());
        assertEquals("42", getResult2.getRecipient());
        assertEquals("Content", getResult2.getContent());
        assertEquals("Emitter", getResult.getEmitter());
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Recipient", getResult.getRecipient());
        assertSame(ofResult, toLocalDateResult);
        assertSame(id, getResult.getId());
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ given {@link ModelMapper} $ then return Empty.
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName("Test findAll() $ given ModelMapper $ then return Empty")
    @Tag("MaintainedByDiffblue")
    void testFindAll$givenModelMapper$thenReturnEmpty() {
        // Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<UserDto> actualFindAllResult = userServiceImpl.findAll();

        // Assert
        verify(userRepository).findAll();
        assertTrue(actualFindAllResult.isEmpty());
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ given {@link ModelMapper} {@link
     * ModelMapper#map(Object, Class)} throw {@link
     * DeleteErrorException#DeleteErrorException(Exception)} with message is {@link
     * Exception#Exception()} $.
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName(
            "Test findAll() $ given ModelMapper map(Object, Class) throw DeleteErrorException(Exception) with message is Exception() $")
    @Tag("MaintainedByDiffblue")
    void testFindAll$givenModelMapperMapThrowDeleteErrorExceptionWithMessageIsException$() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(Mockito.any(), Mockito.<Class<UserDto>>any()))
                .thenThrow(new DeleteErrorException(new Exception()));

        // Act and Assert
        assertThrows(DeleteErrorException.class, () -> userServiceImpl.findAll());
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findAll();
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ given {@link UserDto} (default constructor) Id is
     * randomUUID $ then return first is {@link UserDto} (default constructor).
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName(
            "Test findAll() $ given UserDto (default constructor) Id is randomUUID $ then return first is UserDto (default constructor)")
    @Tag("MaintainedByDiffblue")
    void testFindAll$givenUserDtoIdIsRandomUUID$thenReturnFirstIsUserDto() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");
        when(modelMapper.map(Mockito.any(), Mockito.<Class<UserDto>>any())).thenReturn(userDto);

        // Act
        List<UserDto> actualFindAllResult = userServiceImpl.findAll();

        // Assert
        verify(modelMapper).map(isA(Object.class), isA(Class.class));
        verify(userRepository).findAll();
        assertEquals(1, actualFindAllResult.size());
        assertSame(userDto, actualFindAllResult.get(0));
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ given {@link User#User()} Id is {@code null} $ then
     * return first Id is {@code null}.
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName("Test findAll() $ given User() Id is 'null' $ then return first Id is 'null'")
    @Tag("MaintainedByDiffblue")
    void testFindAll$givenUserIdIsNull$thenReturnFirstIdIsNull() {
        // Arrange
        User user = new User();
        user.setId(null);
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDto> actualFindAllResult =
                new UserServiceImpl(userRepository, new ModelMapper()).findAll();

        // Assert
        verify(userRepository).findAll();
        assertEquals(1, actualFindAllResult.size());
        UserDto getResult = actualFindAllResult.get(0);
        assertEquals("janedoe", getResult.getUsername());
        assertNull(getResult.getId());
        assertTrue(getResult.getMessages().isEmpty());
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ given {@link User#User()} Password is {@code Password}
     * $ then return first Username is {@code Username}.
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName(
            "Test findAll() $ given User() Password is 'Password' $ then return first Username is 'Username'")
    @Tag("MaintainedByDiffblue")
    void testFindAll$givenUserPasswordIsPassword$thenReturnFirstUsernameIsUsername() {
        // Arrange
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setMessages(new ArrayList<>());
        user2.setPassword("Password");
        user2.setUsername("Username");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user2);
        userList.add(user);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<UserDto> actualFindAllResult =
                new UserServiceImpl(userRepository, new ModelMapper()).findAll();

        // Assert
        verify(userRepository).findAll();
        assertEquals(2, actualFindAllResult.size());
        assertEquals("Username", actualFindAllResult.get(0).getUsername());
        UserDto getResult = actualFindAllResult.get(1);
        assertEquals("janedoe", getResult.getUsername());
        assertTrue(getResult.getMessages().isEmpty());
        assertSame(id, getResult.getId());
    }

    /**
     * Test {@link UserServiceImpl#findAll()} $ given {@link User#User()} Password is {@code Password}
     * $ then return second is {@link UserDto} (default constructor).
     *
     * <p>Method under test: {@link UserServiceImpl#findAll()}
     */
    @Test
    @DisplayName(
            "Test findAll() $ given User() Password is 'Password' $ then return second is UserDto (default constructor)")
    @Tag("MaintainedByDiffblue")
    void testFindAll$givenUserPasswordIsPassword$thenReturnSecondIsUserDto() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setMessages(new ArrayList<>());
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setMessages(new ArrayList<>());
        user2.setPassword("Password");
        user2.setUsername("Username");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user2);
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);

        UserDto userDto = new UserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setMessages(new ArrayList<>());
        userDto.setUsername("janedoe");
        when(modelMapper.map(Mockito.any(), Mockito.<Class<UserDto>>any())).thenReturn(userDto);

        // Act
        List<UserDto> actualFindAllResult = userServiceImpl.findAll();

        // Assert
        verify(modelMapper, atLeast(1)).map(Mockito.any(), isA(Class.class));
        verify(userRepository).findAll();
        assertEquals(2, actualFindAllResult.size());
        assertSame(userDto, actualFindAllResult.get(0));
        assertSame(userDto, actualFindAllResult.get(1));
    }
}
