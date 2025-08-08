package com.lucascarvalho.todo_list.dto.User;

public record UserLoginResponseDto(
        String token,
        Long expiresIn
) {
}
