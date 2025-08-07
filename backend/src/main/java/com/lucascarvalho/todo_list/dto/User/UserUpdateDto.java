package com.lucascarvalho.todo_list.dto.User;

import jakarta.validation.constraints.Email;

public record UserUpdateDto(
        String name,
        @Email(message = "Email is invalid")
        String email
) {
}
