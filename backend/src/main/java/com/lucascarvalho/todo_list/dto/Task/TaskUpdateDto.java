package com.lucascarvalho.todo_list.dto.Task;

import com.lucascarvalho.todo_list.entity.enums.Priority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public record TaskUpdateDto(
        String title,
        String description,
        Priority priority,
        @Future(message = "Deadline cannot be in the past of given time")
        OffsetDateTime deadline
) {
}
