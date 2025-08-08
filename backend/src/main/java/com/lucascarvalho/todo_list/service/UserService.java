package com.lucascarvalho.todo_list.service;

import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserResponseDto;
import com.lucascarvalho.todo_list.dto.User.UserUpdateDto;
import com.lucascarvalho.todo_list.entity.User;
import com.lucascarvalho.todo_list.exceptions.ResourceNotFoundException;
import com.lucascarvalho.todo_list.mapper.UserMapper;
import com.lucascarvalho.todo_list.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toResponseDto(user);
    }

    User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    User getUserEntityByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        User newUser = userMapper.toUser(userRequestDto);

        User savedUser = userRepository.save(newUser);
        log.info("User created with id: {}", savedUser.getId());

        return userMapper.toResponseDto(savedUser);
    }

    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userMapper.updateUserFromDto(userUpdateDto, userToUpdate);
        userRepository.save(userToUpdate);

        log.info("User with id {} was updated", userToUpdate.getId());
        return userMapper.toResponseDto(userToUpdate);
    }

    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        log.info("User with id: {} deleted", id);
    }
}
