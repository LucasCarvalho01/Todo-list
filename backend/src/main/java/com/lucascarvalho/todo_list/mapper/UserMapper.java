package com.lucascarvalho.todo_list.mapper;

import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserResponseDto;
import com.lucascarvalho.todo_list.dto.User.UserUpdateDto;
import com.lucascarvalho.todo_list.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    User toUser(UserRequestDto userRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateDto userUpdateDto, @MappingTarget User user);
}
