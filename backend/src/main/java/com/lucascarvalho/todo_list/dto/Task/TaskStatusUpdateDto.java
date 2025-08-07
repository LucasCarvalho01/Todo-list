package com.lucascarvalho.todo_list.dto.Task;

import com.lucascarvalho.todo_list.entity.enums.Status;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateDto(
        @NotNull(message = "Status is required")
        Status status
) {
}
