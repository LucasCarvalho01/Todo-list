package com.lucascarvalho.todo_list.controller;

import java.util.List;
import com.lucascarvalho.todo_list.entity.Task;
import com.lucascarvalho.todo_list.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    List<Task> getAllTasks() {
        return repository.findAll();
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
