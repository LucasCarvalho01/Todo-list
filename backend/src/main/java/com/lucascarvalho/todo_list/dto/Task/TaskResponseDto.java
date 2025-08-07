package com.lucascarvalho.todo_list.dto.Task;

import com.lucascarvalho.todo_list.dto.User.UserResponseDto;

import java.time.OffsetDateTime;

public record TaskResponseDto(
        Long id,
        String title,
        String description,
        String priority,
        String status,
        OffsetDateTime deadline,
        UserResponseDto user
) {
}
