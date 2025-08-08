package com.lucascarvalho.todo_list.controller;

import com.lucascarvalho.todo_list.dto.User.UserLoginRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserLoginResponseDto;
import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserResponseDto;
import com.lucascarvalho.todo_list.entity.User;
import com.lucascarvalho.todo_list.service.AuthService;
import com.lucascarvalho.todo_list.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto registeredUser = authService.signup(userRequestDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> authenticate(@RequestBody UserLoginRequestDto userLoginDto) {
        User authenticatedUser = authService.authenticate(userLoginDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        UserLoginResponseDto loginResponse = new UserLoginResponseDto(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
