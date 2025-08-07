package com.lucascarvalho.todo_list.mapper;

import com.lucascarvalho.todo_list.dto.Task.TaskRequestDto;
import com.lucascarvalho.todo_list.dto.Task.TaskResponseDto;
import com.lucascarvalho.todo_list.dto.Task.TaskUpdateDto;
import com.lucascarvalho.todo_list.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    TaskResponseDto toResponseDto(Task task);

    Task toTask(TaskRequestDto taskRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(TaskUpdateDto taskUpdateDto, @MappingTarget Task task);
}
