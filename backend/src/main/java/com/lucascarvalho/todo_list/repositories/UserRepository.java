package com.lucascarvalho.todo_list.repositories;

import com.lucascarvalho.todo_list.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByName(String name);
}
