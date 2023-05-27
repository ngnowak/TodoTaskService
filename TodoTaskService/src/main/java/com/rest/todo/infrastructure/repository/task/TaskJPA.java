package com.rest.todo.infrastructure.repository.task;

import com.rest.todo.infrastructure.repository.user.UserJPA;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskJPA {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String taskName;

    @Column
    private String description;

    @Column
    private TodoStatus todoStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp dataCreated;

    @UpdateTimestamp
    private Timestamp lastModified;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJPA user;
}
