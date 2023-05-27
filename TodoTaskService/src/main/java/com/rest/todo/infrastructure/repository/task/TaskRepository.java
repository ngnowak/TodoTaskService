package com.rest.todo.infrastructure.repository.task;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<TaskJPA, Long> {

    Iterable<TaskJPA> findAllByTaskNameContaining(String taskName);

    @Query("SELECT t FROM TaskJPA t WHERE t.todoStatus = :todoStatus and t.taskName LIKE %:taskName%")
    Iterable<TaskJPA> findTasksByTodoStatusAndTaskName(
            @Param("taskName") String taskName,
            @Param("todoStatus") TodoStatus status
    );
}
