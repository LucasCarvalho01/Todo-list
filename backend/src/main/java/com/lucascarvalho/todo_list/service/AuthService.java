package com.lucascarvalho.todo_list.service;

import com.lucascarvalho.todo_list.dto.User.UserLoginRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserResponseDto;
import com.lucascarvalho.todo_list.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public UserResponseDto signup(UserRequestDto userSingnup) {
        return userService.saveUser(userSingnup);
    }

    public User authenticate(UserLoginRequestDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.passoword()
                )
        );

        return userService.getUserEntityByEmail(input.email());
    }
}
