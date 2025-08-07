package com.lucascarvalho.todo_list.mapper;

import com.lucascarvalho.todo_list.dto.Task.TaskResponseDto;
import com.lucascarvalho.todo_list.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    @Mapping(source = "user.id", target = "userId")
    TaskResponseDto toResponseDto(Task task);
}
