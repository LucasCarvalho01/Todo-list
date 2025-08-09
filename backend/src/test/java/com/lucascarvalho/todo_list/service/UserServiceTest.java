package com.lucascarvalho.todo_list.service;

import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserResponseDto;
import com.lucascarvalho.todo_list.dto.User.UserUpdateDto;
import com.lucascarvalho.todo_list.entity.User;
import com.lucascarvalho.todo_list.exceptions.ResourceNotFoundException;
import com.lucascarvalho.todo_list.mapper.UserMapper;
import com.lucascarvalho.todo_list.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponseDto userResponseDto;
    private UserRequestDto userRequestDto;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword123");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userResponseDto = new UserResponseDto(
                1L,
                "John Doe",
                "john.doe@example.com"
        );

        userRequestDto = new UserRequestDto(
                "New User",
                "newuser@example.com",
                "password123"
        );

        userUpdateDto = new UserUpdateDto(
                "Updated User",
                "updated@example.com"
        );
    }

    @Test
    @DisplayName("Should return user by ID successfully")
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        // When
        UserResponseDto result = userService.getUserById(1L);

        // Then
        assertThat(result).isEqualTo(userResponseDto);
        verify(userRepository).findById(1L);
        verify(userMapper).toResponseDto(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by ID")
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(999L);
        verify(userMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should return all users successfully")
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        // When
        List<UserResponseDto> result = userService.getAllUsers();

        // Then
        assertThat(result).hasSize(1).containsExactly(userResponseDto);
        verify(userRepository).findAll();
        verify(userMapper).toResponseDto(user);
    }

    @Test
    @DisplayName("Should save user successfully")
    void saveUser_ShouldSaveAndReturnUser() {
        // Given
        User newUser = new User();
        User savedUser = new User();
        savedUser.setId(2L);

        when(userMapper.toUser(userRequestDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.toResponseDto(savedUser)).thenReturn(userResponseDto);

        // When
        UserResponseDto result = userService.saveUser(userRequestDto);

        // Then
        assertThat(result).isEqualTo(userResponseDto);
        verify(userMapper).toUser(userRequestDto);
        verify(userRepository).save(newUser);
        verify(userMapper).toResponseDto(savedUser);
    }

    @Test
    @DisplayName("Should update user successfully")
    void updateUser_ShouldUpdateAndReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        // When
        UserResponseDto result = userService.updateUser(1L, userUpdateDto);

        // Then
        assertThat(result).isEqualTo(userResponseDto);
        verify(userRepository).findById(1L);
        verify(userMapper).updateUserFromDto(userUpdateDto, user);
        verify(userRepository).save(user);
        verify(userMapper).toResponseDto(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent user")
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(999L, userUpdateDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(999L);
        verify(userMapper, never()).updateUserFromDto(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(999L);
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should return user entity by ID successfully")
    void getUserEntityById_ShouldReturnUserEntity_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserEntityById(1L);

        // Then
        assertThat(result).isEqualTo(user);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return user entity by email successfully")
    void getUserEntityByEmail_ShouldReturnUserEntity_WhenUserExists() {
        // Given
        String email = "john.doe@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserEntityByEmail(email);

        // Then
        assertThat(result).isEqualTo(user);
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by email")
    void getUserEntityByEmail_ShouldThrowException_WhenUserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserEntityByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findUserByEmail(email);
    }
}