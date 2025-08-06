package com.lucascarvalho.todo_list.controllers;

import java.util.List;
import com.lucascarvalho.todo_list.entities.User;
import com.lucascarvalho.todo_list.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    List<User> getAllUsers() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    User getUserById(@PathVariable Long id) {
        return repository.findById(id).get();
    }

    @PostMapping()
    User saveUser(@RequestBody User user) {
        return repository.save(user);
    }
}
