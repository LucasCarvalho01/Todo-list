package com.lucascarvalho.todo_list.controller;

import com.lucascarvalho.todo_list.dto.Task.TaskResponseDto;
import com.lucascarvalho.todo_list.entity.Task;
import com.lucascarvalho.todo_list.repository.TaskRepository;
import com.lucascarvalho.todo_list.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository repository;
    private final TaskService taskService;

    public TaskController(TaskRepository repository, TaskService taskService) {
        this.repository = repository;
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> users = taskService.getAllTasks();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    Task getTaskById(@PathVariable Long id) {
        return repository.findById(id).get();
    }

    @PostMapping()
    Task saveTask(@RequestBody Task task) {
        return repository.save(task);
    }
}
