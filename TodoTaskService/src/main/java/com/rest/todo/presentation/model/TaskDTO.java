package com.rest.todo.presentation.model;

import com.rest.todo.infrastructure.repository.task.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TaskDTO {
    public Long id;
    @NotBlank
    public String title;
    public String description;
    @NotNull
    public TodoStatus todoStatus;
    public Timestamp dataCreated;
    public Timestamp lastModified;
    @NotNull
    public UserDTO user;
}
