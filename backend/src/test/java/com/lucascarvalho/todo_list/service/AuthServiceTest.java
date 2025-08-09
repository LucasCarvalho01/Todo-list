package com.lucascarvalho.todo_list.service;

import com.lucascarvalho.todo_list.dto.User.UserLoginRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserResponseDto;
import com.lucascarvalho.todo_list.entity.User;
import com.lucascarvalho.todo_list.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
public class AuthServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private UserLoginRequestDto userLoginRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto(
                "John Doe",
                "john.doe@example.com",
                "plainPassword123"
        );

        userResponseDto = new UserResponseDto(
                1L,
                "John Doe",
                "john.doe@example.com"
        );

        userLoginRequestDto = new UserLoginRequestDto(
                "john.doe@example.com",
                "plainPassword123"
        );

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("$2a$10$encodedPasswordHash");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should delegate signup to UserService successfully")
    void signup_ShouldDelegateToUserService_WhenCalled() {
        // Given
        when(userService.saveUser(userRequestDto)).thenReturn(userResponseDto);

        // When
        UserResponseDto result = authService.signup(userRequestDto);

        // Then
        assertThat(result).isEqualTo(userResponseDto);
        verify(userService).saveUser(userRequestDto);
    }

    @Test
    @DisplayName("Should propagate exception when UserService fails during signup")
    void signup_ShouldPropagateException_WhenUserServiceFails() {
        // Given
        RuntimeException expectedException = new RuntimeException("Email already exists");
        when(userService.saveUser(userRequestDto)).thenThrow(expectedException);

        // When & Then
        assertThatThrownBy(() -> authService.signup(userRequestDto))
                .isEqualTo(expectedException);

        verify(userService).saveUser(userRequestDto);
    }

    @Test
    @DisplayName("Should authenticate user successfully with valid credentials")
    void authenticate_ShouldReturnUser_WhenCredentialsAreValid() {
        // Given
        when(userService.getUserEntityByEmail(userLoginRequestDto.email())).thenReturn(user);

        // When
        User result = authService.authenticate(userLoginRequestDto);

        // Then
        assertThat(result).isEqualTo(user);
        verify(authenticationManager).authenticate(argThat(token ->
                token.getPrincipal().equals(userLoginRequestDto.email()) &&
                        token.getCredentials().equals(userLoginRequestDto.password())
        ));
        verify(userService).getUserEntityByEmail(userLoginRequestDto.email());
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when authentication fails")
    void authenticate_ShouldThrowBadCredentialsException_WhenCredentialsAreInvalid() {
        // Given
        BadCredentialsException expectedException = new BadCredentialsException("Invalid credentials");
        doThrow(expectedException).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // When & Then
        assertThatThrownBy(() -> authService.authenticate(userLoginRequestDto))
                .isEqualTo(expectedException);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).getUserEntityByEmail(anyString());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found after successful authentication")
    void authenticate_ShouldThrowResourceNotFoundException_WhenUserNotFoundAfterAuthentication() {
        // Given
        ResourceNotFoundException expectedException = new ResourceNotFoundException("User not found");
        when(userService.getUserEntityByEmail(userLoginRequestDto.email())).thenThrow(expectedException);

        // When & Then
        assertThatThrownBy(() -> authService.authenticate(userLoginRequestDto))
                .isEqualTo(expectedException);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getUserEntityByEmail(userLoginRequestDto.email());
    }
}