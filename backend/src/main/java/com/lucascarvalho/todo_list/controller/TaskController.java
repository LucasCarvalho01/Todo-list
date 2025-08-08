package com.lucascarvalho.todo_list.controller;

import com.lucascarvalho.todo_list.dto.Task.TaskRequestDto;
import com.lucascarvalho.todo_list.dto.Task.TaskResponseDto;
import com.lucascarvalho.todo_list.dto.Task.TaskStatusUpdateDto;
import com.lucascarvalho.todo_list.dto.Task.TaskUpdateDto;
import com.lucascarvalho.todo_list.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "bearer-jwt")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> users = taskService.getAllTasks();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        TaskResponseDto user = taskService.getTaskById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<TaskResponseDto> saveTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto newTask = taskService.saveTask(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@Valid @RequestBody TaskUpdateDto updateDto, @PathVariable Long id) {
        TaskResponseDto updatedTask = taskService.updateTask(updateDto, id);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> finishTask(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateDto statusDto) {
        TaskResponseDto updatedTask = taskService.updateTaskStatus(id, statusDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
