package com.rest.todo.core.task;

import com.rest.todo.presentation.model.TaskDTO;
import com.rest.todo.infrastructure.repository.task.TaskJPA;
import com.rest.todo.infrastructure.repository.task.TodoStatus;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getTasks();
    TaskDTO getTask(Long id);
    List<TaskDTO> getTasks(String taskName, TodoStatus todoStatus);
    TaskJPA create(TaskJPA task);
    void updateTask(Long id, TaskJPA task);
    void completeTask(Long id);
    void deleteTask(Long id);

}
