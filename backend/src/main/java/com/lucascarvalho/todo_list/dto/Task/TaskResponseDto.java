package com.lucascarvalho.todo_list.dto.Task;

import java.time.OffsetDateTime;

public record TaskResponseDto(
        Long id,
        String title,
        String description,
        String priority,
        String status,
        OffsetDateTime deadline,
        Long userId
) {
}
