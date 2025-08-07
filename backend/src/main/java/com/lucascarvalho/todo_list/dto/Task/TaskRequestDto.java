package com.lucascarvalho.todo_list.dto.Task;

import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import com.lucascarvalho.todo_list.entity.enums.Priority;
import com.lucascarvalho.todo_list.entity.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record TaskRequestDto(
    @NotBlank(message = "Task title is required")
    String title,

    String description,

    @NotBlank(message = "Task priority is required")
    Priority priority,

    @Future(message = "Deadline cannot be in the past of given time")
    OffsetDateTime deadline,

    @Valid
    @NotNull(message = "An user associated is required")
    UserRequestDto user
) {
}
