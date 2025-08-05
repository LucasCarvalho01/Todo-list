package com.lucascarvalho.todo_list.entities;

import com.lucascarvalho.todo_list.entities.enums.Priority;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "TodoItems")
public class TodoItem {
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
    //todo: add resonsible user
}
