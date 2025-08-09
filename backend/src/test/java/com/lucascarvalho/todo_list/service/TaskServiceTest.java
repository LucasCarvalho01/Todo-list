package com.lucascarvalho.todo_list.service;

import com.lucascarvalho.todo_list.dto.Task.TaskRequestDto;
import com.lucascarvalho.todo_list.dto.Task.TaskResponseDto;
import com.lucascarvalho.todo_list.dto.Task.TaskStatusUpdateDto;
import com.lucascarvalho.todo_list.dto.Task.TaskUpdateDto;
import com.lucascarvalho.todo_list.dto.User.UserResponseDto;
import com.lucascarvalho.todo_list.entity.Task;
import com.lucascarvalho.todo_list.entity.User;
import com.lucascarvalho.todo_list.entity.enums.Priority;
import com.lucascarvalho.todo_list.entity.enums.Status;
import com.lucascarvalho.todo_list.exceptions.ResourceNotFoundException;
import com.lucascarvalho.todo_list.mapper.TaskMapper;
import com.lucascarvalho.todo_list.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService Tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User user;
    private TaskResponseDto taskResponseDto;
    private TaskRequestDto taskRequestDto;
    private TaskUpdateDto taskUpdateDto;
    private TaskStatusUpdateDto taskStatusUpdateDto;
    private OffsetDateTime futureDeadline;

    @BeforeEach
    void setUp() {
        futureDeadline = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Description 1");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.IN_PROGRESS);
        task.setDeadline(futureDeadline);
        task.setUser(user);

        UserResponseDto userResponseDto = new UserResponseDto(
                1L,
                "Test User",
                "test@example.com"
        );

        taskResponseDto = new TaskResponseDto(
                1L,
                "Task 1",
                "Description 1",
                "HIGH",
                "IN_PROGRESS",
                futureDeadline,
                userResponseDto
        );

        taskRequestDto = new TaskRequestDto(
                "New Task",
                "New Description",
                Priority.HIGH,
                futureDeadline,
                1L
        );

        taskUpdateDto = new TaskUpdateDto(
                "Updated Task",
                "Updated Description",
                Priority.MEDIUM,
                futureDeadline.plusDays(1)
        );

        taskStatusUpdateDto = new TaskStatusUpdateDto(Status.DONE);
    }

    @Test
    @DisplayName("Should return all tasks successfully")
    void getAllTasks_ShouldReturnAllTasks() {
        // Given
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toResponseDto(task)).thenReturn(taskResponseDto);

        // When
        List<TaskResponseDto> result = taskService.getAllTasks();

        // Then
        assertThat(result).hasSize(1).containsExactly(taskResponseDto);
        verify(taskRepository).findAll();
        verify(taskMapper).toResponseDto(task);
    }

    @Test
    @DisplayName("Should return task by ID successfully")
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDto(task)).thenReturn(taskResponseDto);

        // When
        TaskResponseDto result = taskService.getTaskById(1L);

        // Then
        assertThat(result).isEqualTo(taskResponseDto);
        verify(taskRepository).findById(1L);
        verify(taskMapper).toResponseDto(task);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when task not found by ID")
    void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(999L);
        verify(taskMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should save task with user assignment and default status")
    void saveTask_ShouldSaveTaskWithUserAndDefaultStatus() {
        // Given
        Task newTask = new Task();

        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(taskMapper.toTask(taskRequestDto)).thenReturn(newTask);
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);
        when(taskMapper.toResponseDto(any(Task.class))).thenReturn(taskResponseDto);

        // When
        TaskResponseDto result = taskService.saveTask(taskRequestDto);

        // Then
        assertThat(result).isEqualTo(taskResponseDto);
        verify(userService).getUserEntityById(1L);
        verify(taskMapper).toTask(taskRequestDto);
        verify(taskRepository).save(argThat(t ->
                t.getStatus() == Status.IN_PROGRESS && t.getUser().equals(user)
        ));
        verify(taskMapper).toResponseDto(any(Task.class));
    }

    @Test
    @DisplayName("Should update task successfully")
    void updateTask_ShouldUpdateAndReturnTask() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDto(task)).thenReturn(taskResponseDto);

        // When
        TaskResponseDto result = taskService.updateTask(taskUpdateDto, 1L);

        // Then
        assertThat(result).isEqualTo(taskResponseDto);
        verify(taskRepository).findById(1L);
        verify(taskMapper).updateTaskFromDto(taskUpdateDto, task);
        verify(taskRepository).save(task);
        verify(taskMapper).toResponseDto(task);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent task")
    void updateTask_ShouldThrowException_WhenTaskNotFound() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.updateTask(taskUpdateDto, 999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(999L);
        verify(taskMapper, never()).updateTaskFromDto(any(), any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update task status successfully")
    void updateTaskStatus_ShouldUpdateStatusAndReturnTask() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponseDto(task)).thenReturn(taskResponseDto);

        // When
        TaskResponseDto result = taskService.updateTaskStatus(1L, taskStatusUpdateDto);

        // Then
        assertThat(result).isEqualTo(taskResponseDto);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(argThat(t -> t.getStatus() == Status.DONE));
        verify(taskMapper).toResponseDto(task);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating status of non-existent task")
    void updateTaskStatus_ShouldThrowException_WhenTaskNotFound() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.updateTaskStatus(999L, taskStatusUpdateDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete task successfully")
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent task")
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.deleteTask(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).delete(any());
    }
}