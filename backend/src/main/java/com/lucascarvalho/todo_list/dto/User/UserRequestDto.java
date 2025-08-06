package com.lucascarvalho.todo_list.dto.User;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
    @NotBlank
    String name
) {
}
