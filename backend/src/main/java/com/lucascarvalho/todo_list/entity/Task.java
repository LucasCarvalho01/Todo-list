package com.lucascarvalho.todo_list.entity;

import com.lucascarvalho.todo_list.entity.enums.Priority;
import com.lucascarvalho.todo_list.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
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

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    public Priority priority;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public Status status;

    public OffsetDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
}
