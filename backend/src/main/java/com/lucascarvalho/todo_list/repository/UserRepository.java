package com.lucascarvalho.todo_list.repository;

import com.lucascarvalho.todo_list.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByName(String name);
}
