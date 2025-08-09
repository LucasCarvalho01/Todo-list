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

    private User user1;
    private User user2;
    private UserResponseDto userResponseDto1;
    private UserResponseDto userResponseDto2;
    private UserRequestDto userRequestDto;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("encodedPassword123");
        user1.setCreatedAt(now);
        user1.setUpdatedAt(now);

        user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("encodedPassword456");
        user2.setCreatedAt(now);
        user2.setUpdatedAt(now);

        userResponseDto1 = new UserResponseDto(
                1L,
                "John Doe",
                "john.doe@example.com"
        );

        userResponseDto2 = new UserResponseDto(
                2L,
                "Jane Smith",
                "jane.smith@example.com"
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
    @DisplayName("Should return user when getUserById is called with valid id")
    void getUserById_ShouldReturnUser_WhenValidId() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userMapper.toResponseDto(user1)).thenReturn(userResponseDto1);

        // When
        UserResponseDto result = userService.getUserById(userId);

        // Then
        assertThat(result).isEqualTo(userResponseDto1);
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("John Doe");
        assertThat(result.email()).isEqualTo("john.doe@example.com");
        verify(userRepository).findById(userId);
        verify(userMapper).toResponseDto(user1);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getUserById is called with invalid id")
    void getUserById_ShouldThrowResourceNotFoundException_WhenInvalidId() {
        // Given
        Long invalidId = 999L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(invalidId);
        verify(userMapper, never()).toResponseDto(any(User.class));
    }

    @Test
    @DisplayName("Should return user entity when getUserEntityById is called with valid id")
    void getUserEntityById_ShouldReturnUserEntity_WhenValidId() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        // When
        User result = userService.getUserEntityById(userId);

        // Then
        assertThat(result).isEqualTo(user1);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getUserEntityById is called with invalid id")
    void getUserEntityById_ShouldThrowResourceNotFoundException_WhenInvalidId() {
        // Given
        Long invalidId = 999L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserEntityById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Should return user entity when getUserEntityByEmail is called with valid email")
    void getUserEntityByEmail_ShouldReturnUserEntity_WhenValidEmail() {
        // Given
        String email = "john.doe@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user1));

        // When
        User result = userService.getUserEntityByEmail(email);

        // Then
        assertThat(result).isEqualTo(user1);
        assertThat(result.getEmail()).isEqualTo(email);
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getUserEntityByEmail is called with invalid email")
    void getUserEntityByEmail_ShouldThrowResourceNotFoundException_WhenInvalidEmail() {
        // Given
        String invalidEmail = "nonexistent@example.com";
        when(userRepository.findUserByEmail(invalidEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserEntityByEmail(invalidEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findUserByEmail(invalidEmail);
    }

    @Test
    @DisplayName("Should return all users when getAllUsers is called")
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponseDto(user1)).thenReturn(userResponseDto1);
        when(userMapper.toResponseDto(user2)).thenReturn(userResponseDto2);

        // When
        List<UserResponseDto> result = userService.getAllUsers();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(userResponseDto1, userResponseDto2);
        verify(userRepository).findAll();
        verify(userMapper, times(2)).toResponseDto(any(User.class));
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of());

        // When
        List<UserResponseDto> result = userService.getAllUsers();

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findAll();
        verify(userMapper, never()).toResponseDto(any(User.class));
    }

    @Test
    @DisplayName("Should save user successfully when saveUser is called")
    void saveUser_ShouldSaveUserSuccessfully() {
        // Given
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setName("New User");
        savedUser.setEmail("newuser@example.com");
        savedUser.setPassword("password123");
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser.setUpdatedAt(LocalDateTime.now());

        UserResponseDto expectedResponse = new UserResponseDto(
                3L,
                "New User",
                "newuser@example.com"
        );

        when(userMapper.toUser(userRequestDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.toResponseDto(savedUser)).thenReturn(expectedResponse);

        // When
        UserResponseDto result = userService.saveUser(userRequestDto);

        // Then
        assertThat(result).isEqualTo(expectedResponse);
        assertThat(result.name()).isEqualTo("New User");
        assertThat(result.email()).isEqualTo("newuser@example.com");
        verify(userMapper).toUser(userRequestDto);
        verify(userRepository).save(newUser);
        verify(userMapper).toResponseDto(savedUser);
    }

    @Test
    @DisplayName("Should update user successfully when updateUser is called")
    void updateUser_ShouldUpdateUserSuccessfully() {
        // Given
        Long userId = 1L;
        UserResponseDto updatedResponse = new UserResponseDto(
                1L,
                "Updated User",
                "updated@example.com"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);
        when(userMapper.toResponseDto(user1)).thenReturn(updatedResponse);

        // When
        UserResponseDto result = userService.updateUser(userId, userUpdateDto);

        // Then
        assertThat(result).isEqualTo(updatedResponse);
        verify(userRepository).findById(userId);
        verify(userMapper).updateUserFromDto(userUpdateDto, user1);
        verify(userRepository).save(user1);
        verify(userMapper).toResponseDto(user1);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updateUser is called with invalid id")
    void updateUser_ShouldThrowResourceNotFoundException_WhenInvalidId() {
        // Given
        Long invalidId = 999L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(invalidId, userUpdateDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(invalidId);
        verify(userMapper, never()).updateUserFromDto(any(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully when deleteUser is called")
    void deleteUser_ShouldDeleteUserSuccessfully() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository).findById(userId);
        verify(userRepository).delete(user1);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleteUser is called with invalid id")
    void deleteUser_ShouldThrowResourceNotFoundException_WhenInvalidId() {
        // Given
        Long invalidId = 999L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(invalidId);
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should handle user with valid email format")
    void saveUser_ShouldHandleValidEmailFormat() {
        // Given
        UserRequestDto validEmailUser = new UserRequestDto(
                "Test User",
                "test.user+tag@example.com",
                "password123"
        );

        User mockUser = new User();
        User savedMockUser = new User();
        savedMockUser.setId(1L);

        when(userMapper.toUser(validEmailUser)).thenReturn(mockUser);
        when(userRepository.save(mockUser)).thenReturn(savedMockUser);
        when(userMapper.toResponseDto(savedMockUser)).thenReturn(userResponseDto1);

        // When & Then
        assertThatCode(() -> userService.saveUser(validEmailUser)).doesNotThrowAnyException();
        verify(userRepository).save(mockUser);
    }

    @Test
    @DisplayName("Should handle partial updates correctly")
    void updateUser_ShouldHandlePartialUpdates() {
        // Given
        Long userId = 1L;
        UserUpdateDto partialUpdate = new UserUpdateDto(
                "Only Name Updated",
                null  // Email permanece o mesmo
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);
        when(userMapper.toResponseDto(user1)).thenReturn(userResponseDto1);

        // When
        UserResponseDto result = userService.updateUser(userId, partialUpdate);

        // Then
        assertThat(result).isNotNull();
        verify(userMapper).updateUserFromDto(partialUpdate, user1);
        verify(userRepository).save(user1);
    }
}