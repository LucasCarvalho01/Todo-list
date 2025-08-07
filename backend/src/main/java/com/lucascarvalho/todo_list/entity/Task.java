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
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 511)
    private String description;

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private OffsetDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
