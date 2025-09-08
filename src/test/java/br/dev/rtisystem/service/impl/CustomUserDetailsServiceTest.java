package br.dev.rtisystem.service.impl;

import br.dev.rtisystem.model.entity.User;
import br.dev.rtisystem.repository.UserRepository;
import br.dev.rtisystem.service.impl.auth.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;
    private String username;

    @BeforeEach
    void setUp() {
        username = "usuario_teste";

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword("senha_criptografada");
    }

    @Test
    @DisplayName("Deve carregar usuário por username com sucesso")
    void loadUserByUsernameShouldReturnUserDetails() {
        // Arrange
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails result = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());

        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("USER")));

        verify(userRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é encontrado")
    void loadUserByUsernameShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String nonExistentUsername = "usuario_inexistente";
        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistentUsername));

        assertEquals("Usuário não encontrado: ".concat(nonExistentUsername), exception.getMessage());
        verify(userRepository).findByUsername(nonExistentUsername);
    }
}