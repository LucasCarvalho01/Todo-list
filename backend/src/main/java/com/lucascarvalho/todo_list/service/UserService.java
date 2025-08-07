package com.lucascarvalho.todo_list.service;

import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import com.lucascarvalho.todo_list.dto.User.UserResponseDto;
import com.lucascarvalho.todo_list.dto.User.UserUpdateDto;
import com.lucascarvalho.todo_list.entity.User;
import com.lucascarvalho.todo_list.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponseDto(user.getId(), user.getName());
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getName()))
                .toList();
    }

    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        User newUser = new User();
        newUser.setName(userRequestDto.name());

        User savedUser = userRepository.save(newUser);
        log.info("User created with id: {}", savedUser.getId());

        return new UserResponseDto(savedUser.getId(), savedUser.getName());
    }

    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userToUpdate.setName(userUpdateDto.name());

        userRepository.save(userToUpdate);
        return new UserResponseDto(userToUpdate.getId(), userToUpdate.getName());
    }

    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));

        log.info("User with id: {} deleted", id);
    }
}
