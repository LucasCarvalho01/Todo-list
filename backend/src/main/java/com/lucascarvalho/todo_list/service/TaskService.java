package com.lucascarvalho.todo_list.service;

import com.lucascarvalho.todo_list.dto.Task.TaskRequestDto;
import com.lucascarvalho.todo_list.dto.Task.TaskResponseDto;
import com.lucascarvalho.todo_list.dto.Task.TaskUpdateDto;
import com.lucascarvalho.todo_list.entity.Task;
import com.lucascarvalho.todo_list.entity.User;
import com.lucascarvalho.todo_list.entity.enums.Status;
import com.lucascarvalho.todo_list.exceptions.ResourceNotFoundException;
import com.lucascarvalho.todo_list.mapper.TaskMapper;
import com.lucascarvalho.todo_list.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return taskMapper.toResponseDto(task);
    }

    public TaskResponseDto saveTask(TaskRequestDto request) {
        User user = userService.getUserEntityById(request.userId());

        Task task = taskMapper.toTask(request);
        task.setStatus(Status.IN_PROGRESS);
        task.setUser(user);

        taskRepository.save(task);
        log.info("Task with id {} was created", task.getId());

        return taskMapper.toResponseDto(task);
    }

    public TaskResponseDto updateTask(TaskUpdateDto request, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskMapper.updateTaskFromDto(request, task);
        taskRepository.save(task);

        log.info("Task with id {} was updated", task.getId());
        return taskMapper.toResponseDto(task);
    }

    public void deleteTask(Long id) {
        taskRepository.delete(taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found")));

        log.info("Task with id {} was deleted", id);
    }
}
