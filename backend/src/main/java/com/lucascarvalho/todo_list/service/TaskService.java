package com.lucascarvalho.todo_list.service;

import java.util.List;

import com.lucascarvalho.todo_list.dto.Task.TaskResponseDto;
import com.lucascarvalho.todo_list.entity.Task;
import com.lucascarvalho.todo_list.mapper.TaskMapper;
import com.lucascarvalho.todo_list.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper, TaskRepository taskRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }


}
