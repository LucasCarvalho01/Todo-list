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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    private Task task1;
    private Task task2;
    private User user;
    private UserResponseDto userResponseDto;
    private TaskResponseDto taskResponseDto1;
    private TaskResponseDto taskResponseDto2;
    private TaskRequestDto taskRequestDto;
    private TaskUpdateDto taskUpdateDto;
    private TaskStatusUpdateDto taskStatusUpdateDto;
    private OffsetDateTime futureDeadline;
    private OffsetDateTime pastDeadline;

    @BeforeEach
    void setUp() {
        futureDeadline = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);
        pastDeadline = OffsetDateTime.now(ZoneOffset.UTC).minusDays(1);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        userResponseDto = new UserResponseDto(
                1L,
                "Test User",
                "test@example.com"
        );

        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setPriority(Priority.HIGH);
        task1.setStatus(Status.IN_PROGRESS);
        task1.setDeadline(futureDeadline);
        task1.setUser(user);

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setPriority(Priority.MEDIUM);
        task2.setStatus(Status.DONE);
        task2.setDeadline(futureDeadline.plusDays(3));
        task2.setUser(user);

        taskResponseDto1 = new TaskResponseDto(
                1L,
                "Task 1",
                "Description 1",
                "HIGH",
                "IN_PROGRESS",
                futureDeadline,
                userResponseDto
        );

        taskResponseDto2 = new TaskResponseDto(
                2L,
                "Task 2",
                "Description 2",
                "MEDIUM",
                "DONE",
                futureDeadline.plusDays(3),
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
    @DisplayName("Should return all tasks when getAllTasks is called")
    void getAllTasks_ShouldReturnAllTasks() {
        // Given
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toResponseDto(task1)).thenReturn(taskResponseDto1);
        when(taskMapper.toResponseDto(task2)).thenReturn(taskResponseDto2);

        // When
        List<TaskResponseDto> result = taskService.getAllTasks();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(taskResponseDto1, taskResponseDto2);
        verify(taskRepository).findAll();
        verify(taskMapper, times(2)).toResponseDto(any(Task.class));
    }

    @Test
    @DisplayName("Should return empty list when no tasks exist")
    void getAllTasks_ShouldReturnEmptyList_WhenNoTasksExist() {
        // Given
        when(taskRepository.findAll()).thenReturn(List.of());

        // When
        List<TaskResponseDto> result = taskService.getAllTasks();

        // Then
        assertThat(result).isEmpty();
        verify(taskRepository).findAll();
        verify(taskMapper, never()).toResponseDto(any(Task.class));
    }

    @Test
    @DisplayName("Should return task when getTaskById is called with valid id")
    void getTaskById_ShouldReturnTask_WhenValidId() {
        // Given
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task1));
        when(taskMapper.toResponseDto(task1)).thenReturn(taskResponseDto1);

        // When
        TaskResponseDto result = taskService.getTaskById(taskId);

        // Then
        assertThat(result).isEqualTo(taskResponseDto1);
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Task 1");
        assertThat(result.priority()).isEqualTo("HIGH");
        assertThat(result.status()).isEqualTo("IN_PROGRESS");
        assertThat(result.deadline()).isEqualTo(futureDeadline);
        verify(taskRepository).findById(taskId);
        verify(taskMapper).toResponseDto(task1);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getTaskById is called with invalid id")
    void getTaskById_ShouldThrowResourceNotFoundException_WhenInvalidId() {
        // Given
        Long invalidId = 999L;
        when(taskRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.getTaskById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(invalidId);
        verify(taskMapper, never()).toResponseDto(any(Task.class));
    }

    @Test
    @DisplayName("Should save task successfully when saveTask is called")
    void saveTask_ShouldSaveTaskSuccessfully() {
        // Given
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setPriority(Priority.HIGH);
        newTask.setDeadline(futureDeadline);

        Task savedTask = new Task();
        savedTask.setId(3L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setPriority(Priority.HIGH);
        savedTask.setStatus(Status.IN_PROGRESS);
        savedTask.setDeadline(futureDeadline);
        savedTask.setUser(user);

        TaskResponseDto expectedResponse = new TaskResponseDto(
                3L,
                "New Task",
                "New Description",
                "HIGH",
                "IN_PROGRESS",
                futureDeadline,
                userResponseDto
        );

        when(userService.getUserEntityById(taskRequestDto.userId())).thenReturn(user);
        when(taskMapper.toTask(taskRequestDto)).thenReturn(newTask);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toResponseDto(any(Task.class))).thenReturn(expectedResponse);

        // When
        TaskResponseDto result = taskService.saveTask(taskRequestDto);

        // Then
        assertThat(result).isEqualTo(expectedResponse);
        assertThat(result.status()).isEqualTo("IN_PROGRESS");
        verify(userService).getUserEntityById(taskRequestDto.userId());
        verify(taskMapper).toTask(taskRequestDto);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toResponseDto(any(Task.class));

        // Verify that the task status was set to IN_PROGRESS and user was assigned
        verify(taskRepository).save(argThat(task ->
                task.getStatus() == Status.IN_PROGRESS &&
                        task.getUser().equals(user)
        ));
    }

    @Test
    @DisplayName("Should update task successfully when updateTask is called")
    void updateTask_ShouldUpdateTaskSuccessfully() {
        // Given
        Long taskId = 1L;
        TaskResponseDto updatedResponse = new TaskResponseDto(
                1L,
                "Updated Task",
                "Updated Description",
                "MEDIUM",
                "IN_PROGRESS",
                futureDeadline.plusDays(1),
                userResponseDto
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task1));
        when(taskRepository.save(task1)).thenReturn(task1);
        when(taskMapper.toResponseDto(task1)).thenReturn(updatedResponse);

        // When
        TaskResponseDto result = taskService.updateTask(taskUpdateDto, taskId);

        // Then
        assertThat(result).isEqualTo(updatedResponse);
        verify(taskRepository).findById(taskId);
        verify(taskMapper).updateTaskFromDto(taskUpdateDto, task1);
        verify(taskRepository).save(task1);
        verify(taskMapper).toResponseDto(task1);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updateTask is called with invalid id")
    void updateTask_ShouldThrowResourceNotFoundException_WhenInvalidId() {
        // Given
        Long invalidId = 999L;
        when(taskRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.updateTask(taskUpdateDto, invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(invalidId);
        verify(taskMapper, never()).updateTaskFromDto(any(), any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update task status successfully when updateTaskStatus is called")
    void updateTaskStatus_ShouldUpdateStatusSuccessfully() {
        // Given
        Long taskId = 1L;
        TaskResponseDto updatedResponse = new TaskResponseDto(
                1L,
                "Task 1",
                "Description 1",
                "HIGH",
                "DONE",
                futureDeadline,
                userResponseDto
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task1));
        when(taskRepository.save(task1)).thenReturn(task1);
        when(taskMapper.toResponseDto(task1)).thenReturn(updatedResponse);

        // When
        TaskResponseDto result = taskService.updateTaskStatus(taskId, taskStatusUpdateDto);

        // Then
        assertThat(result).isEqualTo(updatedResponse);
        assertThat(result.status()).isEqualTo("DONE");
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task1);
        verify(taskMapper).toResponseDto(task1);

        // Verify that the status was actually updated
        verify(taskRepository).save(argThat(task ->
                task.getStatus() == taskStatusUpdateDto.status()
        ));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updateTaskStatus is called with invalid id")
    void updateTaskStatus_ShouldThrowResourceNotFoundException_WhenInvalidId() {
        // Given
        Long invalidId = 999L;
        when(taskRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.updateTaskStatus(invalidId, taskStatusUpdateDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(invalidId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete task successfully when deleteTask is called")
    void deleteTask_ShouldDeleteTaskSuccessfully() {
        // Given
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task1));

        // When
        taskService.deleteTask(taskId);

        // Then
        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task1);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleteTask is called with invalid id")
    void deleteTask_ShouldThrowResourceNotFoundException_WhenInvalidId() {
        // Given
        Long invalidId = 999L;
        when(taskRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.deleteTask(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");

        verify(taskRepository).findById(invalidId);
        verify(taskRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should handle different priority levels correctly")
    void saveTask_ShouldHandleDifferentPriorities() {
        // Given
        TaskRequestDto highPriorityTask = new TaskRequestDto(
                "High Priority Task",
                "Critical task",
                Priority.HIGH,
                futureDeadline,
                1L
        );

        TaskRequestDto lowPriorityTask = new TaskRequestDto(
                "Low Priority Task",
                "Not urgent task",
                Priority.LOW,
                futureDeadline,
                1L
        );

        Task mockTask = new Task();
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(taskMapper.toTask(any())).thenReturn(mockTask);
        when(taskRepository.save(any())).thenReturn(mockTask);
        when(taskMapper.toResponseDto(any())).thenReturn(taskResponseDto1);

        // When & Then
        assertThatCode(() -> taskService.saveTask(highPriorityTask)).doesNotThrowAnyException();
        assertThatCode(() -> taskService.saveTask(lowPriorityTask)).doesNotThrowAnyException();

        verify(taskRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("Should handle status transitions correctly")
    void updateTaskStatus_ShouldHandleStatusTransitions() {
        // Given
        Long taskId = 1L;
        TaskStatusUpdateDto statusToDone = new TaskStatusUpdateDto(Status.DONE);
        TaskStatusUpdateDto statusToInProgress = new TaskStatusUpdateDto(Status.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any())).thenReturn(task1);
        when(taskMapper.toResponseDto(any())).thenReturn(taskResponseDto1);

        // When & Then
        assertThatCode(() -> taskService.updateTaskStatus(taskId, statusToDone)).doesNotThrowAnyException();
        assertThatCode(() -> taskService.updateTaskStatus(taskId, statusToInProgress)).doesNotThrowAnyException();

        verify(taskRepository, times(2)).save(argThat(task ->
                task.getStatus() == Status.DONE || task.getStatus() == Status.IN_PROGRESS
        ));
    }
}