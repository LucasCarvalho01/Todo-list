package com.lucascarvalho.todo_list.dto.Task;

import com.lucascarvalho.todo_list.entity.enums.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record TaskRequestDto(
    @NotBlank(message = "Task title is required")
    String title,

    String description,

    @NotNull(message = "Task priority is required")
    Priority priority,

    @FutureOrPresent(message = "Deadline cannot be in the past of given time")
    OffsetDateTime deadline,

    @NotNull(message = "An user associated is required")
    Long userId
) {
}
