package com.lucascarvalho.todo_list.entity;

import com.lucascarvalho.todo_list.entity.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "description", length = 511)
    public String description;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    public Priority priority;

    public Date deadline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User userId;
}
