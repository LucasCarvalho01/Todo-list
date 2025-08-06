package com.lucascarvalho.todo_list.entity;

import com.lucascarvalho.todo_list.dto.User.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    public User(UserRequestDto userDto) {
        this.name = userDto.name();
    }
}
